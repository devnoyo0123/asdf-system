package com.example.paymentservice.domain.exception;

public class PaymentNotFoundException extends PaymentDomainException{

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
