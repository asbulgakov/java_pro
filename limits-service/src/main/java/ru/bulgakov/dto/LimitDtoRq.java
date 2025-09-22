package ru.bulgakov.dto;

import java.math.BigDecimal;

public record LimitDtoRq(
        Long userId,
        BigDecimal amount
) {
}
