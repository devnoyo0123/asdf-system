package com.example.paymentservice.application.ports.output.message.publisher;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.paymentservice.domain.event.PaymentCancelledEvent;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent> {
}
