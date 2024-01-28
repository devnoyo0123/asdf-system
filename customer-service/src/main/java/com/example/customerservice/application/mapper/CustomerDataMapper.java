package com.example.customerservice.application.mapper;

import com.example.customerservice.application.dto.CustomerQueryResponse;
import com.example.customerservice.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {
    public CustomerQueryResponse customerToCustomerFindOneQueryResponse(Customer customer) {
        return CustomerQueryResponse.of(
                customer.getId().getValue(),
                customer.getPhone(),
                customer.getName(),
                customer.getAddress().getStreet()
        );
    }
}
