package ru.bulgakov.spring.exception;

public class NoPaymentMethodsException extends RuntimeException {
    public NoPaymentMethodsException(String message) {
        super(message);
    }
}
