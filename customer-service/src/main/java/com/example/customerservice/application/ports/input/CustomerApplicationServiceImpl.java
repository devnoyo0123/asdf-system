package com.example.customerservice.application.ports.input;

import com.example.customerservice.application.CustomerDashboardQueryHandler;
import com.example.customerservice.application.CustomerQueryHandler;
import com.example.customerservice.application.dto.CustomerDashboardQuery;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.dto.CustomerQueryResponse;
import com.example.customerservice.application.dto.FindOneCustomerQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerApplicationServiceImpl implements CustomerApplicationService{

    private final CustomerQueryHandler customerQueryHandler;
    private final CustomerDashboardQueryHandler customerDashboardQueryHandler;

    public CustomerApplicationServiceImpl(CustomerQueryHandler customerQueryHandler, CustomerDashboardQueryHandler customerDashboardQueryHandler) {
        this.customerQueryHandler = customerQueryHandler;
        this.customerDashboardQueryHandler = customerDashboardQueryHandler;
    }

    @Override
    public CustomerQueryResponse findOneCustomerBy(FindOneCustomerQuery findOneCustomerQuery) {
        return customerQueryHandler.findOneBy(findOneCustomerQuery);
    }

    @Override
    public Page<CustomerDashboardQueryResponse> search(CustomerDashboardQuery customerDashboardQuery) {
        return customerDashboardQueryHandler.search(customerDashboardQuery);
    }
}
