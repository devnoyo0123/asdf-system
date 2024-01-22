package com.example.customerservice.application.ports.output.repository;

import com.example.customerservice.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findOneCustomerBy(UUID id);
}
