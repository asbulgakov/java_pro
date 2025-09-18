package ru.bulgakov.spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bulgakov.spring.client.ProductClient;
import ru.bulgakov.spring.dto.payment.PaymentDtoRq;
import ru.bulgakov.spring.dto.payment.PaymentDtoRs;
import ru.bulgakov.spring.dto.product.ProductDtoRs;
import ru.bulgakov.spring.dto.product.UserProductsResponseRs;
import ru.bulgakov.spring.exception.BalanceUpdateException;
import ru.bulgakov.spring.exception.InsufficientFundsException;
import ru.bulgakov.spring.exception.NoPaymentMethodsException;
import ru.bulgakov.spring.exception.PaymentProcessingException;
import ru.bulgakov.spring.exception.ProductNotFoundException;
import ru.bulgakov.spring.model.Payment;
import ru.bulgakov.spring.model.PaymentStatus;
import ru.bulgakov.spring.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ProductClient productClient;
    private final BalanceServiceImpl balanceService;

    @Override
    public UserProductsResponseRs getUserProducts(Long userId) {
        try {
            List<ProductDtoRs> products = productClient.getUserProducts(userId);

            String message = products.isEmpty() ?
                    "У вас нет привязанных карт или счетов" :
                    "Найдено " + products.size() + " способов оплаты";

            return new UserProductsResponseRs(userId, products, message);

        } catch (RuntimeException e) {
            log.error("Ошибка при получении продуктов пользователя {}: {}", userId, e.getMessage());
            return new UserProductsResponseRs(
                    userId,
                    List.of(),
                    "Временные проблемы с загрузкой способов оплаты"
            );
        }
    }

    @Override
    @Transactional
    public PaymentDtoRs processPayment(PaymentDtoRq paymentRequest) {
        log.info("Начало обработки платежа для пользователя: {}, продукта: {}, сумма: {}",
                paymentRequest.userId(), paymentRequest.productId(), paymentRequest.amount());

        validatePaymentRequest(paymentRequest);

        UserProductsResponseRs response = getUserProducts(paymentRequest.userId());
        List<ProductDtoRs> products = response.products();

        if (products.isEmpty()) {
            throw new NoPaymentMethodsException("У пользователя нет привязанных карт или счетов для оплаты");
        }

        ProductDtoRs product = findUserProduct(products, paymentRequest.productId(), paymentRequest.userId());

        validateSufficientFunds(product.balance(), paymentRequest.amount());

        Payment payment = createPayment(paymentRequest);
        Payment savedPayment = paymentRepository.save(payment);
        log.info("Платеж создан с ID: {}", savedPayment.getId());

        try {
            balanceService.updateProductBalance(product, paymentRequest.amount());

            payment.setStatus(PaymentStatus.COMPLETED);
            payment = paymentRepository.save(payment);
            log.info("Платеж успешно завершен: {}", payment.getId());

        } catch (BalanceUpdateException e) {
            handlePaymentFailure(payment, e);
            throw new PaymentProcessingException("Не удалось завершить платеж. Пожалуйста, попробуйте позже");
        }

        return convertToDto(payment);
    }

    private ProductDtoRs findUserProduct(List<ProductDtoRs> products, Long productId, Long userId) {
        return products.stream()
                .filter(p -> p.id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException(
                        "Выбранный способ оплаты не найден. Пожалуйста, выберите другой способ оплаты"
                ));
    }

    private void validateSufficientFunds(BigDecimal currentBalance, BigDecimal requiredAmount) {
        if (currentBalance.compareTo(requiredAmount) < 0) {
            java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
            String errorMessage = String.format(
                    "Недостаточно средств для совершения платежа. " +
                            "Доступно: %s руб., требуется: %s руб. " +
                            "Пожалуйста, пополните счет или выберите другой способ оплаты.",
                    df.format(currentBalance), df.format(requiredAmount)
            );
            throw new InsufficientFundsException(errorMessage);
        }
    }

    private Payment createPayment(PaymentDtoRq paymentRequest) {
        return Payment.builder()
                .userId(paymentRequest.userId())
                .productId(paymentRequest.productId())
                .amount(paymentRequest.amount())
                .description(paymentRequest.description())
                .status(PaymentStatus.PROCESSING)
                .build();
    }

    private void handlePaymentFailure(Payment payment, BalanceUpdateException e) {
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
        log.error("Платеж завершился ошибкой: {}. Причина: {}", payment.getId(), e.getMessage());
    }

    private PaymentDtoRs convertToDto(Payment payment) {
        return new PaymentDtoRs(
                payment.getId(),
                payment.getAmount(),
                payment.getDescription(),
                payment.getStatus(),
                payment.getProductId(),
                payment.getUserId(),
                payment.getConfirmationCode()
        );
    }

    private void validatePaymentRequest(PaymentDtoRq paymentRequest) {
        if (paymentRequest.userId() == null || paymentRequest.userId() <= 0) {
            throw new PaymentProcessingException("Неверный идентификатор пользователя");
        }
        if (paymentRequest.productId() == null || paymentRequest.productId() <= 0) {
            throw new PaymentProcessingException("Не выбран способ оплаты");
        }
        if (paymentRequest.amount() == null || paymentRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new PaymentProcessingException("Сумма платежа должна быть положительной");
        }

        if (paymentRequest.amount().compareTo(new BigDecimal("1000000")) > 0) {
            throw new PaymentProcessingException("Сумма платежа превышает максимальный лимит");
        }
    }
}
