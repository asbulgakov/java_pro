package ru.bulgakov.spring.exception;

public class CreatedUserException extends RuntimeException {
    public CreatedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
