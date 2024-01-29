package com.example.restaurantservice.domain.entity;

import com.example.modulecommon.domain.entity.BaseEntity;
import com.example.modulecommon.domain.valueobject.*;
import com.example.restaurantservice.domain.valueobject.OrderApprovalId;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class OrderApproval extends BaseEntity<OrderApprovalId> {
    private final RestaurantId restaurantId;
    private final OrderId orderId;
    private final OrderDetail orderDetail;
    private OrderApprovalStatus approvalStatus;

    private OrderApproval(Builder builder) {
        setId(builder.orderApprovalId);
        restaurantId = builder.restaurantId;
        orderId = builder.orderId;
        approvalStatus = builder.approvalStatus;
        orderDetail = builder.orderDetail;
    }

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

    public static Builder builder() {
        return new Builder();
    }

    public void changeOrderApprovalStatus(OrderApprovalStatus orderApprovalStatus) {
        this.approvalStatus = orderApprovalStatus;

    }

    public static final class Builder {
        private OrderApprovalId orderApprovalId;
        private RestaurantId restaurantId;
        private OrderId orderId;
        private OrderApprovalStatus approvalStatus;
        private OrderDetail orderDetail;

        private Builder() {
        }

        public Builder orderApprovalId(OrderApprovalId val) {
            orderApprovalId = val;
            return this;
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder orderId(OrderId val) {
            orderId = val;
            return this;
        }

        public Builder approvalStatus(OrderApprovalStatus val) {
            approvalStatus = val;
            return this;
        }

        public Builder orderDetail(OrderDetail val) {
            orderDetail = val;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(this);
        }
    }
}
