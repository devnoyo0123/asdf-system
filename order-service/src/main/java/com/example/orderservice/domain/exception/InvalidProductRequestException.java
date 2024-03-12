package com.example.orderservice.domain.exception;

import com.example.modulecommon.domain.exception.DomainException;

public class InvalidProductRequestException extends DomainException {
    public InvalidProductRequestException(String message) {
        super(message);
    }

    public InvalidProductRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
