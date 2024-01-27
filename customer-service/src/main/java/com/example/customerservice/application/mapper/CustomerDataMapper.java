package com.example.customerservice.application.mapper;

import com.example.customerservice.application.dto.CustomerFindOneQueryResponse;
import com.example.customerservice.domain.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {
    public CustomerFindOneQueryResponse customerToCustomerFindOneQueryResponse(Customer customer) {
        return CustomerFindOneQueryResponse.of(
                customer.getId().getValue(),
                customer.getPhone(),
                customer.getName(),
                customer.getAddress().getStreet()
        );
    }
}
