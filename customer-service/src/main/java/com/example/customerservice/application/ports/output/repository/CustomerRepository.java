package com.example.customerservice.application.ports.output.repository;

import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.dto.CustomerSearchCondition;
import com.example.customerservice.domain.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findOneCustomerBy(UUID id);

    Page<CustomerDashboardQueryResponse> search(
            CustomerSearchCondition condition,
            Pageable pageable) ;
}
