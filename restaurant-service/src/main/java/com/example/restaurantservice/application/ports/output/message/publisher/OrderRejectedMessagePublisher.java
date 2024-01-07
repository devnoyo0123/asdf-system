package com.example.restaurantservice.application.ports.output.message.publisher;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.restaurantservice.domain.event.OrderRejectedEvent;

public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent> {
}
