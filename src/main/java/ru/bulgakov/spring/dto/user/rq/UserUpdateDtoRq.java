package ru.bulgakov.spring.dto.user.rq;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserUpdateDtoRq(
        @JsonProperty("id")
        Long id,
        @JsonProperty("username")
        String username
) {

}
