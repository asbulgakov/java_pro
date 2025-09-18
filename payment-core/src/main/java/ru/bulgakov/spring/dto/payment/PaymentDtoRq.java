package ru.bulgakov.spring.dto.payment;

import java.math.BigDecimal;

public record PaymentDtoRq(
        Long userId,
        Long productId,
        BigDecimal amount,
        String description
) {
}
