package ru.bulgakov.spring.exception;

public class DeletedUserException extends RuntimeException {
    public DeletedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
