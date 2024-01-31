package com.example.customerservice.adapter.dataaccess.mapper;

import com.example.customerservice.adapter.dataaccess.entity.CustomerEntity;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.domain.entity.Customer;
import com.example.modulecommon.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataAccessMapper {

    public Customer customerEntityToCustomer(CustomerEntity customerEntity) {
        return Customer.builder()
                .id(CustomerId.of(customerEntity.getId()))
                .name(customerEntity.getName())
                .address(customerEntity.getAddress())
                .phone(customerEntity.getPhone())
                .build();
    }

    public CustomerDashboardQueryResponse customerEntityToCustomerDashboardQueryResponse(CustomerEntity customerEntity) {
        return CustomerDashboardQueryResponse.of(
                customerEntity.getId(),
                customerEntity.getName(),
                customerEntity.getAddress().getStreet(),
                customerEntity.getAddress().getDetails());
    }
}
