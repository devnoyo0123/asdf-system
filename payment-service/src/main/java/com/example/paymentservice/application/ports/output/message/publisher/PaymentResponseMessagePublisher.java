package com.example.paymentservice.application.ports.output.message.publisher;


import com.example.modulecommon.outbox.OutboxStatus;
import com.example.paymentservice.domain.outbox.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface PaymentResponseMessagePublisher {
    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
