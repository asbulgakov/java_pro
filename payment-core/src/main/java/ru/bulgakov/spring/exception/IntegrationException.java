package ru.bulgakov.spring.exception;

import lombok.Getter;

@Getter
public class IntegrationException extends RuntimeException {
    private final String externalMessage;

    public IntegrationException(String message, String externalMessage) {
        super(message);
        this.externalMessage = externalMessage;
    }

}
