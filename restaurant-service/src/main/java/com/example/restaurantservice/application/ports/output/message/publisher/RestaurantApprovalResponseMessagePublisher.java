package com.example.restaurantservice.application.ports.output.message.publisher;


import com.example.modulecommon.outbox.OutboxStatus;
import com.example.restaurantservice.domain.entity.outbox.OrderOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalResponseMessagePublisher {

    void publish(OrderOutboxMessage orderOutboxMessage,
                 BiConsumer<OrderOutboxMessage, OutboxStatus> outboxCallback);
}
