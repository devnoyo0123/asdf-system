package com.example.paymentservice.domain.event;

import com.example.paymentservice.application.ports.output.message.PaymentEventPublisher;
import com.example.paymentservice.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCancelledEvent extends PaymentEvent {

    private final PaymentEventPublisher<PaymentCancelledEvent> domainEventPublisher;

    public PaymentCancelledEvent(Payment payment,
                                 ZonedDateTime createdAt, PaymentEventPublisher<PaymentCancelledEvent> domainEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        this.domainEventPublisher.publish(this);
    }
}
