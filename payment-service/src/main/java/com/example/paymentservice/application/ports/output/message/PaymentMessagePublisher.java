package com.example.paymentservice.application.ports.output.message;

public interface PaymentMessagePublisher<T> {
    void publish(T domainEvent);
}
