package ru.bulgakov.spring.dto.product.rs;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.bulgakov.spring.model.ProductType;

import java.math.BigDecimal;

public record ProductDtoRs(
        @JsonProperty("id")
        Long id,
        @JsonProperty("accountNumber")
        String accountNumber,
        @JsonProperty("balance")
        BigDecimal balance,
        @JsonProperty("productType")
        ProductType productType,
        @JsonProperty("userId")
        Long userId,
        @JsonProperty("username")
        String username
) {
}
