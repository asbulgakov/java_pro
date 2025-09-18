package ru.bulgakov.spring.dto.product.rq;

import ru.bulgakov.spring.model.ProductType;

import java.math.BigDecimal;

public record ProductCreateRq(
        String accountNumber,
        BigDecimal balance,
        ProductType productType,
        Long userId
) {
}
