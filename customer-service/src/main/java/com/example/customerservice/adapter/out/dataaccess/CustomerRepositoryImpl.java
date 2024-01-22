package com.example.customerservice.adapter.out.dataaccess;

import com.example.customerservice.adapter.out.dataaccess.mapper.CustomerDataAccessMapper;
import com.example.customerservice.adapter.out.dataaccess.repository.CustomerJpaRepository;
import com.example.customerservice.application.ports.output.repository.CustomerRepository;
import com.example.customerservice.domain.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;
    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryImpl(CustomerJpaRepository customerJpaRepository, CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJpaRepository = customerJpaRepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Optional<Customer> findOneCustomerBy(UUID id) {
        return customerJpaRepository.findById(id).map(customerDataAccessMapper::customerEntityToCustomer);
    }
}
