package com.example.customerservice.domain.exception;

import com.example.modulecommon.domain.exception.DomainException;

public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
