package com.example.orderservice.adapter.out.scheduler.exception;

public class PaymentOutboxSchedulerException extends RuntimeException{
    public PaymentOutboxSchedulerException(String message) {
        super(message);
    }
}
