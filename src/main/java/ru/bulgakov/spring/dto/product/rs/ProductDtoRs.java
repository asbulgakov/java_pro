package ru.bulgakov.spring.dto.product.rs;

import ru.bulgakov.spring.model.ProductType;

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
