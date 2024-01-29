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
    private boolean isActive;
    private List<Product> products;
    public void setActive(boolean active) {
        isActive = active;
    }

    private Restaurant(Builder builder) {
        setId(builder.id);
        isActive = builder.isActive;
        products = builder.products;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId id;
        private boolean isActive;
        private List<Product> products;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            id = val;
            return this;
        }

        public Builder isActive(boolean val) {
            isActive = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
