package ru.bulgakov.spring.dto.product;

import java.math.BigDecimal;

public record ProductDtoRs(
        Long id,
        String accountNumber,
        BigDecimal balance,
        ProductType productType,
        Long userId,
        String username
) {
}
