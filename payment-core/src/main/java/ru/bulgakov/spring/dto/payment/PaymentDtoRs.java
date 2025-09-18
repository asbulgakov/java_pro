package ru.bulgakov.spring.dto.payment;

import ru.bulgakov.spring.model.PaymentStatus;

import java.math.BigDecimal;

public record PaymentDtoRs(
        Long id,
        BigDecimal amount,
        String description,
        PaymentStatus status,
        Long productId,
        Long userId,
        String confirmationCode
) {
}
