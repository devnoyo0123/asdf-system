package com.example.orderservice.application.ports.output.customer.repository;

import com.example.orderservice.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);
}
