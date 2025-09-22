package ru.bulgakov.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bulgakov.exception.InsufficientLimitException;
import ru.bulgakov.exception.InvalidAmountException;
import ru.bulgakov.exception.LimitNotFoundException;
import ru.bulgakov.exception.NegativeLimitException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            InsufficientLimitException.class,
            InvalidAmountException.class,
            NegativeLimitException.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExceptions(RuntimeException ex) {
        String errorCode = getErrorCode(ex);
        log.warn("Bad request exception ({}): {}", errorCode, ex.getMessage());

        return new ErrorResponse(ex.getMessage(), errorCode);
    }

    @ExceptionHandler(LimitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleLimitNotFoundException(LimitNotFoundException ex) {
        log.warn("Limit not found exception: {}", ex.getMessage());

        return new ErrorResponse(ex.getMessage(),"LIMIT_NOT_FOUND");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(RuntimeException ex) {
        log.error("Unexpected error occurred: ", ex);

        return new ErrorResponse("Internal server error. Please contact support.", "INTERNAL_SERVER_ERROR");
    }

    private String getErrorCode(RuntimeException ex) {
        if (ex instanceof InsufficientLimitException) {
            return "INSUFFICIENT_LIMIT";
        } else if (ex instanceof InvalidAmountException) {
            return "INVALID_AMOUNT";
        } else if (ex instanceof NegativeLimitException) {
            return "NEGATIVE_LIMIT";
        } else if (ex instanceof IllegalArgumentException) {
            return "ILLEGAL_ARGUMENT";
        }
        return "UNKNOWN_ERROR";
    }
}
