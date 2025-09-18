package ru.bulgakov.spring.dto.product;


import java.math.BigDecimal;

public record ProductUpdateRq(
        Long id,
        String accountNumber,
        BigDecimal balance,
        ProductType productType
) {
}
