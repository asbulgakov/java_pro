package ru.bulgakov.spring.dto.product.rq;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.bulgakov.spring.model.ProductType;

import java.math.BigDecimal;

public record ProductCreateRq(
        @JsonProperty("accountNumber")
        String accountNumber,
        @JsonProperty("balance")
        BigDecimal balance,
        @JsonProperty("productType")
        ProductType productType,
        @JsonProperty("userId")
        Long userId
) {
}
