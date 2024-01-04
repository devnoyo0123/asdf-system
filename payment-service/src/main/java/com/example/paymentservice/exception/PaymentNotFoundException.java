package com.example.paymentservice.exception;

import com.example.modulecommon.domain.exception.DomainException;

public class PaymentNotFoundException extends PaymentDomainException{

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
