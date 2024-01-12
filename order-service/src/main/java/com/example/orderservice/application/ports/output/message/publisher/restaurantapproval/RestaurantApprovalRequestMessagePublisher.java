package com.example.orderservice.application.ports.output.message.publisher.restaurantapproval;

import com.example.modulecommon.outbox.OutboxStatus;
import com.example.orderservice.domain.outbox.approval.OrderApprovalOutboxMessage;

import java.util.function.BiConsumer;

public interface RestaurantApprovalRequestMessagePublisher {
    void publish(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                 BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallback);

}
