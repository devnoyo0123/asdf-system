package com.example.restaurantservice.application.ports.output.message.publisher;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.restaurantservice.domain.event.OrderApprovalEvent;
import com.example.restaurantservice.domain.event.OrderApprovedEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent> {
}
