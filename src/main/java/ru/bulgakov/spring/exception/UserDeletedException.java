package ru.bulgakov.spring.exception;

public class UserDeletedException extends RuntimeException {

    public UserDeletedException(String message) {
        super(message);
    }

    public UserDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
