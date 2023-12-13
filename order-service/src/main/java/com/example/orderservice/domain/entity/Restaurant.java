package com.example.orderservice.domain.entity;

import com.example.modulecommon.domain.entity.AggregateRoot;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import lombok.Getter;

import java.util.List;

@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {
    public Restaurant(List<Product> productLists, boolean active) {
        this.productLists = productLists;
        this.active = active;
    }

    private final List<Product> productLists;
    private boolean active;

    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        productLists = builder.productLists;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> productLists;
        private boolean active;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder productLists(List<Product> val) {
            productLists = val;
            return this;
        }

        public Builder active(boolean val) {
            active = val;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
