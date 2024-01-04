package com.example.paymentservice.application.exception;

import com.example.paymentservice.domain.exception.PaymentDomainException;

public class PaymentApplicationServiceException extends PaymentDomainException {

    public PaymentApplicationServiceException(String message) {
        super(message);
    }

    public PaymentApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

