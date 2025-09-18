package ru.bulgakov.spring.service;

import ru.bulgakov.spring.dto.product.ProductDtoRs;

import java.math.BigDecimal;

public interface BalanceService {
    void updateProductBalance(ProductDtoRs product, BigDecimal amountToDeduct);
    BigDecimal calculateNewBalance(ProductDtoRs product, BigDecimal amountToDeduct);
}
