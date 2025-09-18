package ru.bulgakov.spring.dto.product;

import java.util.List;

public record UserProductsResponseRs(
        Long userId,
        List<ProductDtoRs> products,
        String message
) {
}
