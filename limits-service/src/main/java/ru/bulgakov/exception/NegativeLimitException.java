package ru.bulgakov.exception;

public class NegativeLimitException extends RuntimeException {
    public NegativeLimitException(String message) {
        super(message);
    }
}
