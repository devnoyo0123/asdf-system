package com.example.customerservice.adapter.out.dataaccess.repository;

import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.dto.CustomerSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerCustomRepository {
    Page<CustomerDashboardQueryResponse> search(
            CustomerSearchCondition condition,
            Pageable pageable) ;
}
