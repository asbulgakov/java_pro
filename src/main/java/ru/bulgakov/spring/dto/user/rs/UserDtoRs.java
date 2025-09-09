package ru.bulgakov.spring.dto.user.rs;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDtoRs(
        @JsonProperty("id")
        Long id,
        @JsonProperty("username")
        String username
) {
}
