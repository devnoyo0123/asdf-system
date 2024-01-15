package com.example.orderservice.adapter.out.dataaccess.outbox.payment.exception;

public class PaymentOutboxNotFoundException extends RuntimeException {

        public PaymentOutboxNotFoundException(String message) {
            super(message);
        }
}
