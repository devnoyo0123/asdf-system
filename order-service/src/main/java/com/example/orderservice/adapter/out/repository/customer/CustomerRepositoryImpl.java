package com.example.orderservice.adapter.out.repository.customer;

import com.example.orderservice.application.mapper.customer.CustomerDataAccessMapper;
import com.example.orderservice.application.ports.output.customer.repository.CustomerJpaRepository;
import com.example.orderservice.application.ports.output.customer.repository.CustomerRepository;
import com.example.orderservice.domain.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository,
                                  CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return Optional.empty();
    }
}
