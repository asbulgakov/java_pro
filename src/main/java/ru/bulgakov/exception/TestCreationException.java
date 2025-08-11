package ru.bulgakov.exception;

public class TestCreationException extends RuntimeException {
    public TestCreationException(String msg, Exception ex) {
        super(msg, ex);
    }
}
