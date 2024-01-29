package com.example.orderservice.application.ports.output.customer.executor;

import com.example.modulecommon.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerExecutor {
    Optional<Customer> getCustomerBy(UUID id);
}
