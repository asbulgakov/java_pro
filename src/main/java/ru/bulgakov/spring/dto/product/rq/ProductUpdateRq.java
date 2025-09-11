package ru.bulgakov.spring.dto.product.rq;

import ru.bulgakov.spring.model.ProductType;

import java.math.BigDecimal;

public record ProductUpdateRq(
        Long id,
        String accountNumber,
        BigDecimal balance,
        ProductType productType
) {
}
