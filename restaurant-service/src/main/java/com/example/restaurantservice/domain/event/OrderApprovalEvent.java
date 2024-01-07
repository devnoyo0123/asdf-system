package com.example.restaurantservice.domain.event;

import com.example.modulecommon.domain.event.DomainEvent;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.restaurantservice.domain.entity.OrderApproval;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public abstract class OrderApprovalEvent implements DomainEvent<OrderApproval> {
    private final OrderApproval orderApproval;
    private final RestaurantId restaurantId;
    private final List<String> failureMessages;
    private final ZonedDateTime createdAt;

    protected OrderApprovalEvent(OrderApproval orderApproval,
                                 RestaurantId restaurantId,
                                 List<String> failureMessages,
                                 ZonedDateTime createdAt) {
        this.orderApproval = orderApproval;
        this.restaurantId = restaurantId;
        this.failureMessages = failureMessages;
        this.createdAt = createdAt;
    }
}
