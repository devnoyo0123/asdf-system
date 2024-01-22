package com.example.restaurantservice.domain;

import com.example.modulecommon.domain.event.publisher.DomainEventPublisher;
import com.example.modulecommon.domain.valueobject.OrderApprovalStatus;
import com.example.restaurantservice.application.ports.output.message.publisher.OrderApprovedMessagePublisher;
import com.example.restaurantservice.application.ports.output.message.publisher.OrderRejectedMessagePublisher;
import com.example.restaurantservice.domain.entity.Restaurant;
import com.example.restaurantservice.domain.event.OrderApprovalEvent;
import com.example.restaurantservice.domain.event.OrderApprovedEvent;
import com.example.restaurantservice.domain.event.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
public class RestaurantDomainService {

    public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                     List<String> failureMessages) {
        restaurant.validateOrder(failureMessages);
        log.info("Validationg order with id: {}", restaurant.getOrderDetail().getId().getValue());

        if (failureMessages.isEmpty()) {
            log.info("Order is approved for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))));
        } else {
            log.info("Order is rejected for order id: {}", restaurant.getOrderDetail().getId().getValue());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))));
        }
    }
}
