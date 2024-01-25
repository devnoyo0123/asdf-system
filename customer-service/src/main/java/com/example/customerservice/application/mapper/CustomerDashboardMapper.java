package com.example.customerservice.application.mapper;

import com.example.customerservice.adapter.dto.CustomerDashboardQueryRequest;
import com.example.customerservice.application.dto.CustomerDashboardQuery;
import com.example.customerservice.application.dto.CustomerSearchCondition;
import com.example.modulecommon.domain.constant.SortCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerDashboardMapper {
    public CustomerDashboardQuery customerDashboardQueryRequestToCustomerDashboardQuery(CustomerDashboardQueryRequest customerDashboardQueryRequest) {
        return CustomerDashboardQuery.of(
                new CustomerSearchCondition(
                        customerDashboardQueryRequest.getName(),
                        customerDashboardQueryRequest.getStreet(),
                        SortCriteria.builder().key("name").build()),
                PageRequest.of(
                        customerDashboardQueryRequest.getPage(),
                        customerDashboardQueryRequest.getSize())
        );
    }
}
