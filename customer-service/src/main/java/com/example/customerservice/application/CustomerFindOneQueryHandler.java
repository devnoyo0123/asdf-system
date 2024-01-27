package com.example.customerservice.application;

import com.example.customerservice.application.dto.CustomerFindOneQueryResponse;
import com.example.customerservice.application.dto.FindOneCustomerQuery;
import com.example.customerservice.application.mapper.CustomerDataMapper;
import com.example.customerservice.application.ports.output.repository.CustomerRepository;
import com.example.customerservice.domain.entity.Customer;
import com.example.customerservice.domain.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class CustomerFindOneQueryHandler {
    private final CustomerDataMapper customerDataMapper;
    private final CustomerRepository customerRepository;

    public CustomerFindOneQueryHandler(CustomerDataMapper customerDataMapper, CustomerRepository customerRepository) {
        this.customerDataMapper = customerDataMapper;
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = false)
    public CustomerFindOneQueryResponse findOneCustomerBy(FindOneCustomerQuery findOneCustomerQuery) {
        Optional<Customer> customerResult = customerRepository.findOneCustomerBy(findOneCustomerQuery.id());
        if(customerResult.isEmpty()) {
            log.warn("Could not find customer with id: {}", findOneCustomerQuery.id());
            throw new CustomerNotFoundException("Could not find customer with id: "+findOneCustomerQuery.id());
        }
        return customerDataMapper.customerToCustomerFindOneQueryResponse(customerResult.get());
    }
}
