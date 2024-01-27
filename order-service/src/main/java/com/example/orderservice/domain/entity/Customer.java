package com.example.orderservice.domain.entity;

import com.example.modulecommon.domain.entity.AggregateRoot;
import com.example.modulecommon.domain.valueobject.CustomerId;
import lombok.Getter;

@Getter
public class Customer extends AggregateRoot<CustomerId> {
    private String name;
    private String phone;
    private String street;

    protected Customer(CustomerId customerId, String name, String phone, String street) {
        super.setId(customerId);
        this.name = name;
        this.phone = phone;
        this.street = street;
    }

    protected Customer(CustomerId customerId) {
        super.setId(customerId);
    }

    public static Customer of(CustomerId customerId, String name, String phone, String street) {
        return new Customer(customerId, name, phone, street);
    }
}
