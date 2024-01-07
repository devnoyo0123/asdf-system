package com.example.restaurantservice.domain.entity;

import com.example.modulecommon.domain.entity.AggregateRoot;
import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.OrderApprovalStatus;
import com.example.modulecommon.domain.valueobject.OrderStatus;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.restaurantservice.domain.valueobject.OrderApprovalId;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
    private OrderApproval orderApproval;
    private boolean isActive;
    private final OrderDetail orderDetail;

    public void validateOrder(List<String> failureMessages) {
        if( orderDetail.getOrderStatus() != OrderStatus.PAID ) {
            failureMessages.add("Payment is not completed for order: " + orderDetail.getId());
        }
        Money totalAmount = orderDetail.getProducts().stream().map(product -> {
            if (product.isAvailable()) {
                failureMessages.add("Product with id: " + product.getId().getValue()
                        + " is not available");
            }
            return product.getPrice().multiply(product.getQuantity());
        }).reduce(Money.ZERO, Money::add);

        if (!totalAmount.equals(orderDetail.getTotalAmount())) {
            failureMessages.add("Price total is not correct for order: " + orderDetail.getId());
        }
    }

    public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
        orderApproval = OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .restaurantId(this.getId())
                .orderId(orderDetail.getId())
                .approvalStatus(orderApprovalStatus)
                .build();
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private Restaurant(Builder builder) {
        setId(builder.id);
        orderApproval = builder.orderApproval;
        isActive = builder.isActive;
        orderDetail = builder.orderDetail;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId id;
        private OrderApproval orderApproval;
        private boolean isActive;
        private OrderDetail orderDetail;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            id = val;
            return this;
        }

        public Builder orderApproval(OrderApproval val) {
            orderApproval = val;
            return this;
        }

        public Builder isActive(boolean val) {
            isActive = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
