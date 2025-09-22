package ru.bulgakov.dto;

import java.math.BigDecimal;

public record LimitUpdateDtoRq(
        BigDecimal newDailyLimit
) {
}
