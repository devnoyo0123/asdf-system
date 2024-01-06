package com.example.paymentservice.domain.event;


import com.example.paymentservice.application.ports.output.message.PaymentEventPublisher;
import com.example.paymentservice.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;

public class PaymentFailedEvent extends PaymentEvent {

    private final PaymentEventPublisher<PaymentFailedEvent> domainEventPublisher;

    public PaymentFailedEvent(Payment payment,
                              ZonedDateTime createdAt,
                              List<String> failureMessages, PaymentEventPublisher<PaymentFailedEvent> domainEventPublisher) {
        super(payment, createdAt, failureMessages);
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        domainEventPublisher.publish(this);
    }
}
