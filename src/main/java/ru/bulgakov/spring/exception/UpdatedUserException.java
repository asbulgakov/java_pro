package ru.bulgakov.spring.exception;

public class UpdatedUserException extends RuntimeException {
    public UpdatedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
