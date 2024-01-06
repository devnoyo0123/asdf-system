package com.example.paymentservice.application.ports.output.exception;

public class PaymentDataAccessException extends RuntimeException {

    public PaymentDataAccessException(String message) {
        super(message);
    }

    public PaymentDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
