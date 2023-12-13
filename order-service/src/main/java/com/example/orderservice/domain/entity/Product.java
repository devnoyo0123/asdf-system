package com.example.orderservice.domain.entity;

import com.example.modulecommon.domain.entity.BaseEntity;
import com.example.modulecommon.domain.valueobject.Money;
import com.example.modulecommon.domain.valueobject.ProductId;
import lombok.Getter;


@Getter
public class Product extends BaseEntity<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId productId, String name, Money price) {
        super.setId(productId);
        this.name = name;
        this.price = price;
    }

    public Product(ProductId productId) {
        super.setId(productId);
    }

    public void updateWithConfirmedNameAndPrice(String name, Money price) {
        this.name = name;
        this.price = price;
    }
}
