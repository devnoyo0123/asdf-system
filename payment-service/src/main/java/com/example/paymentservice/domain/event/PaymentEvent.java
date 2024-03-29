package com.example.paymentservice.domain.event;


import com.example.modulecommon.domain.event.DomainEvent;
import com.example.paymentservice.domain.entity.Payment;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public abstract class PaymentEvent implements DomainEvent<Payment> {

    private final Payment payment;
    private final ZonedDateTime createdAt;
    private final List<String> failureMessages;

    public PaymentEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages) {
        this.payment = payment;
        this.createdAt = createdAt;
        this.failureMessages = failureMessages;
    }
}
