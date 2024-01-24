package com.example.customerservice.adapter.out.dataaccess.repository;


import com.example.customerservice.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findCustomer(UUID customerId);
}
