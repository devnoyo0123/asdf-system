package com.example.paymentservice.domain.event;


import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.paymentservice.application.ports.output.message.PaymentEventPublisher;
import com.example.paymentservice.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCompletedEvent extends PaymentEvent {

    private final PaymentEventPublisher<PaymentCompletedEvent> domainEventPublisher;

    public PaymentCompletedEvent(Payment payment,
                                 ZonedDateTime createdAt, PaymentEventPublisher<PaymentCompletedEvent> domainEventPublisher) {
        super(payment, createdAt, Collections.emptyList());
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public void fire() {
        domainEventPublisher.publish(this);
    }
}
