package com.example.restaurantservice.domain.entity;

import com.example.modulecommon.domain.entity.BaseEntity;
import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.ProductId;
import lombok.Getter;

@Getter
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;
    private int quantity;
    private boolean isAvailable;

    public void updateWithConfirmedNameAndPriceAndAvailability(String name, Money price, int quantity, boolean isAvailable) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
    }

    private Product(Builder builder) {
        setId(builder.productId);
        name = builder.name;
        price = builder.price;
        quantity = builder.quantity;
        isAvailable = builder.isAvailable;
    }

    public static Builder builder() {
        return new Builder();
    }


    public static final class Builder {
        private ProductId productId;
        private String name;
        private Money price;
        private int quantity;
        private boolean isAvailable;

        private Builder() {
        }


        public Builder productId(ProductId val) {
            productId = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder isAvailable(boolean val) {
            isAvailable = val;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
