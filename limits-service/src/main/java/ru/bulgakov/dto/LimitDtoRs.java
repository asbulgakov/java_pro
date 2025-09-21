package ru.bulgakov.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LimitDtoRs(
        Long userId,
        BigDecimal dailyLimit,
        BigDecimal remainingLimit,
        LocalDateTime lastResetDate
) {
}
