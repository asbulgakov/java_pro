package ru.bulgakov.spring.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bulgakov.spring.exception.ProductAlreadyExistsException;
import ru.bulgakov.spring.exception.ProductDeletionException;
import ru.bulgakov.spring.exception.ProductNotFoundException;
import ru.bulgakov.spring.exception.UserAlreadyExistsException;
import ru.bulgakov.spring.exception.UserDeletedException;
import ru.bulgakov.spring.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ErrorRs handleUserNotFound(UserNotFoundException ex) {
        String msg = ex.getMessage();
        return new ErrorRs(HttpStatus.NOT_FOUND.value(), msg);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ErrorRs handleUserAlreadyExists(UserAlreadyExistsException ex) {
        String msg = ex.getMessage();
        return new ErrorRs(HttpStatus.CONFLICT.value(), msg);
    }

    @ExceptionHandler(UserDeletedException.class)
    public ErrorRs handleUserDeletionException(UserDeletedException ex) {
        String msg = ex.getMessage();
        return new ErrorRs(HttpStatus.CONFLICT.value(), msg);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorRs handleProductNotFound(ProductNotFoundException ex) {
        String msg = ex.getMessage();
        return new ErrorRs(HttpStatus.NOT_FOUND.value(), msg);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ErrorRs handleProductAlreadyExists(ProductAlreadyExistsException ex) {
        String msg = ex.getMessage();
        return new ErrorRs(HttpStatus.CONFLICT.value(), msg);
    }

    @ExceptionHandler(ProductDeletionException.class)
    public ErrorRs handleProductDeletionException(ProductDeletionException ex) {
        String msg = ex.getMessage();
        return new ErrorRs(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorRs handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage();
        return new ErrorRs(HttpStatus.BAD_REQUEST.value(), msg);
    }
}
