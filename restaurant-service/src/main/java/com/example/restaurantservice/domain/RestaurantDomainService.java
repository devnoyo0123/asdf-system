package com.example.restaurantservice.domain;

import com.example.modulecommon.domain.valueobject.OrderApprovalStatus;
import com.example.restaurantservice.application.dto.RestaurantApprovalRequest;
import com.example.restaurantservice.domain.entity.OrderApproval;
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
                                            OrderApproval orderApproval,
                                            List<String> failureMessages) {
        orderApproval.validateOrder(failureMessages);
        log.debug("Validate order with id: {}", orderApproval.getOrderDetail().getId().getValue());

        if (failureMessages.isEmpty()) {
            log.debug("Order is approved for order id: {}", orderApproval.getOrderDetail().getId().getValue());
            orderApproval.changeOrderApprovalStatus(OrderApprovalStatus.APPROVED);
            return new OrderApprovedEvent(orderApproval,
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))));
        } else {
            log.debug("Order is rejected for order id: {}", orderApproval.getOrderDetail().getId().getValue());
            orderApproval.changeOrderApprovalStatus(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(orderApproval,
                    restaurant.getId(),
                    failureMessages,
                    ZonedDateTime.now(ZoneId.of(String.valueOf(UTC))));
        }
    }

}
