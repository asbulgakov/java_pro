package ru.bulgakov.spring.dto.user.rq;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDtoRq(
        @JsonProperty("username")
        String username
) {
}
