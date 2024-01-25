package com.example.customerservice.application.mapper;

import com.example.customerservice.adapter.dto.CustomerDashboardQueryRequest;
import com.example.customerservice.application.dto.CustomerDashboardQuery;
import com.example.customerservice.application.dto.CustomerFindOneQueryResponse;
import com.example.customerservice.application.dto.CustomerSearchCondition;
import com.example.customerservice.domain.entity.Customer;
import com.example.modulecommon.domain.constant.SortCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerDataMapper {
    public CustomerFindOneQueryResponse customerToCustomerFindOneQueryResponse(Customer customer) {
        return CustomerFindOneQueryResponse.of(customer.getId().getValue());
    }

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
