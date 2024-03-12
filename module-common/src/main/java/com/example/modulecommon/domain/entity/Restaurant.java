package com.example.modulecommon.domain.entity;

import com.example.modulecommon.domain.valueobject.RestaurantId;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class Restaurant extends AggregateRoot<RestaurantId> {

    private final List<Product> products;
    private boolean active;


    private Restaurant(Builder builder) {
        super.setId(builder.restaurantId);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private List<Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder restaurantId(RestaurantId val) {
            restaurantId = val;
            return this;
        }

        public Builder products(List<Product> val) {
            products = val;
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