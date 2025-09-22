package ru.bulgakov.handler;

public record ErrorResponse(
        String message,
        String errorCode
) {
}
