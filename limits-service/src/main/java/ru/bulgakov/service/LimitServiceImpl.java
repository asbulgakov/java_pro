package ru.bulgakov.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bulgakov.dto.LimitDtoRq;
import ru.bulgakov.dto.LimitDtoRs;
import ru.bulgakov.exception.InsufficientLimitException;
import ru.bulgakov.exception.InvalidAmountException;
import ru.bulgakov.exception.LimitNotFoundException;
import ru.bulgakov.exception.NegativeLimitException;
import ru.bulgakov.mapper.LimitMapper;
import ru.bulgakov.model.UserLimit;
import ru.bulgakov.repository.LimitRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {
    private static final BigDecimal DEFAULT_DAILY_LIMIT = new BigDecimal("10000.00");
    private final LimitRepository limitRepository;
    private final LimitMapper limitMapper;

    @Override
    @Transactional
    public LimitDtoRs getOrCreateUserLimit(Long userId) {
        UserLimit limit = getLimitByUserId(userId)
                .orElseGet(() -> createDefaultUserLimit(userId));
        if (isResetNeeded(limit.getLastResetDate())) {
            reset(limit, LocalDateTime.now());
            limit = limitRepository.save(limit);
        }
        return limitMapper.toDto(limit);
    }

    @Override
    @Transactional
    public LimitDtoRs checkAndReductionLimit(LimitDtoRq limitDtoRq) {
        validateAmount(limitDtoRq.amount());
        UserLimit limit = getLimitByUserId(limitDtoRq.userId())
                .orElseGet(() -> createDefaultUserLimit(limitDtoRq.userId()));
        if (isResetNeeded(limit.getLastResetDate())) {
            reset(limit, LocalDateTime.now());
        }
        if (!hasSufficientLimit(limit, limitDtoRq.amount())) {
            throw new InsufficientLimitException(
                    String.format("Insufficient limit. Requested: %s, Available: %s",
                            limitDtoRq.amount(), limit.getRemainingLimit())
            );
        }
        deduct(limit, limitDtoRq.amount());
        UserLimit reductionLimit = limitRepository.save(limit);
        return limitMapper.toDto(reductionLimit);
    }

    @Override
    @Transactional
    public LimitDtoRs updateDailyLimit(LimitDtoRq limitDtoRq) {
        if (limitDtoRq.amount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeLimitException("Daily limit cannot be negative");
        }
        UserLimit limit = getLimitByUserId(limitDtoRq.userId())
                .orElseThrow(() -> new LimitNotFoundException(
                        String.format("Limit not found for user ID: %d", limitDtoRq.userId())
                        ));
        updateLimit(limit, limitDtoRq.amount());
        UserLimit updatedLimit = limitRepository.save(limit);
        return limitMapper.toDto(updatedLimit);
    }

    @Override
    @Transactional
    public LimitDtoRs refundLimit(LimitDtoRq limitDtoRq) {
        validateAmount(limitDtoRq.amount());
        UserLimit limit = getLimitByUserId(limitDtoRq.userId())
                .orElseThrow(() -> new LimitNotFoundException(
                        String.format("Limit not found for user ID: %d", limitDtoRq.userId())
                ));
        refund(limit, limitDtoRq.amount());
        UserLimit refundedLimit = limitRepository.save(limit);
        return limitMapper.toDto(refundedLimit);
    }

    @Override
    @Transactional
    @Scheduled(cron = "${scheduling.cron.reset-time}")
    public void resetAllLimits() {
        List<UserLimit> allLimits = limitRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (UserLimit limit : allLimits) {
            reset(limit, now);
        }
        limitRepository.saveAll(allLimits);
    }

    private Optional<UserLimit> getLimitByUserId(Long userId) {
        return limitRepository.findByUserId(userId);
    }

    private UserLimit createDefaultUserLimit(Long userId) {
        UserLimit newUserLimit = ru.bulgakov.model.UserLimit.builder()
                .userId(userId)
                .dailyLimit(DEFAULT_DAILY_LIMIT)
                .remainingLimit(DEFAULT_DAILY_LIMIT)
                .lastResetDate(LocalDateTime.now())
                .build();
        return limitRepository.save(newUserLimit);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
    }

    private boolean isResetNeeded(LocalDateTime lastResetDate) {
        if (lastResetDate == null) {
            return true;
        }
        return !lastResetDate.toLocalDate().equals(LocalDate.now());
    }

    private void deduct(UserLimit limit, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Deduction amount must be positive");
        }

        BigDecimal newRemaining = limit.getRemainingLimit().subtract(amount);
        limit.setRemainingLimit(newRemaining);
    }

    private boolean hasSufficientLimit(UserLimit limit, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        return limit.getRemainingLimit().compareTo(amount) >= 0;
    }

    private void refund(UserLimit limit, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Refund amount must be positive");
        }
        BigDecimal newRemaining = limit.getRemainingLimit().add(amount);

        if (newRemaining.compareTo(limit.getDailyLimit()) > 0) {
            limit.setRemainingLimit(limit.getDailyLimit());
        } else {
            limit.setRemainingLimit(newRemaining);
        }
    }

    private void updateLimit(UserLimit limit, BigDecimal newDailyLimit) {
        BigDecimal oldDailyLimit = limit.getDailyLimit();

        if (newDailyLimit.compareTo(oldDailyLimit) > 0) {
            BigDecimal difference = newDailyLimit.subtract(oldDailyLimit);
            limit.setRemainingLimit(limit.getRemainingLimit().add(difference));
        } else if (newDailyLimit.compareTo(limit.getRemainingLimit()) < 0) {
            limit.setRemainingLimit(newDailyLimit);
        }

        limit.setDailyLimit(newDailyLimit);
    }

    private void reset(UserLimit limit, LocalDateTime resetTime) {
        limit.setRemainingLimit(limit.getDailyLimit());
        limit.setLastResetDate(resetTime);
    }
}
