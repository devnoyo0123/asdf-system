package com.example.paymentservice.application.ports.output.message.publisher;

import com.example.modulecommon.domain.event.DomainEvent;

public interface PaymentEventPublisher<T extends DomainEvent> {
    void publish(T domainEvent);
}
