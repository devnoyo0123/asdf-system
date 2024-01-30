package com.example.modulecommon.domain.entity;

import com.example.modulecommon.domain.entity.BaseEntity;
import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.ProductId;
import lombok.Getter;


@Getter
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    protected Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    protected Product(ProductId productId) {
        super.setId(productId);
    }

    public static Product of(ProductId productId, String name, Money price) {
        return new Product(productId, name, price);
    }

    public static Product of(ProductId productId) {
        return new Product(productId);
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
