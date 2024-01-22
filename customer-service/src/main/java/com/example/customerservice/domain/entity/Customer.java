package com.example.customerservice.domain.entity;

import com.example.modulecommon.domain.entity.AggregateRoot;
import com.example.modulecommon.domain.valueobject.Address;
import com.example.modulecommon.domain.valueobject.CustomerId;
import lombok.Getter;

@Getter
public class Customer extends AggregateRoot<CustomerId> {
    private final String name;
    private final String phone;
    private final String email;
    private final Address address;

    private Customer(Builder builder) {
        super.setId(builder.customerId);
        name = builder.name;
        phone = builder.phone;
        email = builder.email;
        address = builder.address;
    }
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String name;
        private String phone;
        private String email;
        private Address address;
        private CustomerId customerId;

        private Builder() {
        }


        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder phone(String val) {
            phone = val;
            return this;
        }

        public Builder email(String val) {
            email = val;
            return this;
        }

        public Builder address(Address val) {
            address = val;
            return this;
        }

        public Builder id(CustomerId val) {
            customerId = val;
            return this;
        }

        public Customer build() {
            return new Customer(this);
        }
    }
}
