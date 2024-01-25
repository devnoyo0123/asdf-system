package com.example.customerservice.application;

import com.example.customerservice.application.dto.CustomerDashboardQuery;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.ports.output.repository.CustomerRepository;
import com.example.customerservice.domain.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class CustomerDashboardQueryHandler {

    private final CustomerRepository customerRepository;

    public CustomerDashboardQueryHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Page<CustomerDashboardQueryResponse> search(CustomerDashboardQuery customerDashboardQuery) {
        return customerRepository.search(customerDashboardQuery.searchCondition(), customerDashboardQuery.pageable());
    }
}
