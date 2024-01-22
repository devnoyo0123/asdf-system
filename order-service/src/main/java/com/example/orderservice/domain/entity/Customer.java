package com.example.orderservice.domain.entity;

import com.example.modulecommon.domain.entity.AggregateRoot;
import com.example.modulecommon.domain.valueobject.CustomerId;

public class Customer extends AggregateRoot<CustomerId> {
    private String name;
    private String phone;
    private String email;

    public Customer(CustomerId customerId, String name, String phone, String email) {
        super.setId(customerId);
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
