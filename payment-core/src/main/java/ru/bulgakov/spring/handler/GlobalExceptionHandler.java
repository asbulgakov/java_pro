package ru.bulgakov.spring.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bulgakov.spring.exception.BalanceUpdateException;
import ru.bulgakov.spring.exception.InsufficientFundsException;
import ru.bulgakov.spring.exception.IntegrationException;
import ru.bulgakov.spring.exception.NoPaymentMethodsException;
import ru.bulgakov.spring.exception.PaymentProcessingException;
import ru.bulgakov.spring.exception.ProductNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoPaymentMethodsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoPaymentMethods(NoPaymentMethodsException ex) {
        return new ErrorResponse(ex.getMessage(), "NO_PAYMENT_METHODS");
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProductNotFound(ProductNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), "PRODUCT_NOT_FOUND");
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInsufficientFunds(InsufficientFundsException ex) {
        return new ErrorResponse(ex.getMessage(), "INSUFFICIENT_FUNDS");
    }

    @ExceptionHandler(PaymentProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlePaymentProcessing(PaymentProcessingException ex) {
        return new ErrorResponse(ex.getMessage(), "PAYMENT_PROCESSING_ERROR");
    }

    @ExceptionHandler(BalanceUpdateException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleBalanceUpdate(BalanceUpdateException ex) {
        return new ErrorResponse("Ошибка при обработке платежа", "BALANCE_UPDATE_ERROR");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(RuntimeException ex) {
        return new ErrorResponse("Внутренняя ошибка сервера", "INTERNAL_ERROR");
    }

    @ExceptionHandler(IntegrationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleIntegrationException(IntegrationException ex) {
        return new ErrorResponse(
                ex.getExternalMessage(),
                "INTEGRATION_ERROR"
        );
    }
}
