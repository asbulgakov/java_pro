package ru.bulgakov.exception;

public class TestMethodException extends RuntimeException {
    public TestMethodException(String message, IllegalAccessException ex) {
        super(message, ex);
    }

    public TestMethodException(String msg, ConversionException ex) {
        super(msg, ex);
    }
}
