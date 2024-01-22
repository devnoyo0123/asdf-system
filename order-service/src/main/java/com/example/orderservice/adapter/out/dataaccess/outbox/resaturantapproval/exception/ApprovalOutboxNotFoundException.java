package com.example.orderservice.adapter.out.dataaccess.outbox.resaturantapproval.exception;

public class ApprovalOutboxNotFoundException extends RuntimeException {

    public ApprovalOutboxNotFoundException(String message) {
        super(message);
    }
}