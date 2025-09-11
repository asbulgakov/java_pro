package ru.bulgakov.spring.handler;

import lombok.Builder;

@Builder
public record ErrorRs(
    int status,
    String message
){
}
