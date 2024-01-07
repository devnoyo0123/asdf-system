package com.example.orderservice.adapter.out.dataaccess.customer.mapper;

import com.example.modulecommon.domain.valueobject.CustomerId;
import com.example.orderservice.application.ports.output.customer.entity.CustomerEntity;
import com.example.orderservice.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return new Customer(new CustomerId(customerEntity.getId()));
    }

    public CustomerEntity customerToCustomerEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId().getValue())
                .username(customer.getUsername())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
