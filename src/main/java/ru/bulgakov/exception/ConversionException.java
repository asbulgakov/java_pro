package ru.bulgakov.exception;

public class ConversionException extends RuntimeException {
    public ConversionException(String msg, Exception ex) {
        super(msg, ex);
    }

    public ConversionException(String message) {
        super(message);
    }
}
