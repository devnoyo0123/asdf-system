package com.example.orderservice.application.ports.output.message.publisher.payment;

import com.example.modulecommon.outbox.OutboxStatus;
import com.example.orderservice.domain.outbox.payment.OrderPaymentOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentRequestMessagePublisher {
    void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallback);
}
