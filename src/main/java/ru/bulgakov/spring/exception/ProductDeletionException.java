package ru.bulgakov.spring.exception;

public class ProductDeletionException extends  RuntimeException {
    public ProductDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
