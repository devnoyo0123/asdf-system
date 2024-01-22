package com.example.customerservice.adapter.out.dataaccess.mapper;

import com.example.customerservice.adapter.out.dataaccess.entity.CustomerEntity;
import com.example.customerservice.domain.entity.Customer;
import com.example.modulecommon.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return Customer.builder()
                .id(new CustomerId(customerEntity.getId()))
                .name(customerEntity.getName())
                .address(customerEntity.getAddress())
                .phone(customerEntity.getPhone())
                .build();
    }

}
