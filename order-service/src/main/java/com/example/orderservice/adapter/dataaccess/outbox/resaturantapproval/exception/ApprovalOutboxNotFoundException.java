package com.example.orderservice.adapter.dataaccess.outbox.resaturantapproval.exception;

public class ApprovalOutboxNotFoundException extends RuntimeException {

    public ApprovalOutboxNotFoundException(String message) {
        super(message);
    }
}