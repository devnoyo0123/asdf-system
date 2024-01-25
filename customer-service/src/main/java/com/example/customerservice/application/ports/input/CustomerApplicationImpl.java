package com.example.customerservice.application.ports.input;

import com.example.customerservice.application.CustomerDashboardQueryHandler;
import com.example.customerservice.application.CustomerFindOneQueryHandler;
import com.example.customerservice.application.dto.CustomerDashboardQuery;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.dto.CustomerFindOneQueryResponse;
import com.example.customerservice.application.dto.FindOneCustomerQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerApplicationImpl implements CustomerApplicationService{

    private final CustomerFindOneQueryHandler customerFindOneQueryHandler;
    private final CustomerDashboardQueryHandler customerDashboardQueryHandler;

    public CustomerApplicationImpl(CustomerFindOneQueryHandler customerFindOneQueryHandler, CustomerDashboardQueryHandler customerDashboardQueryHandler) {
        this.customerFindOneQueryHandler = customerFindOneQueryHandler;
        this.customerDashboardQueryHandler = customerDashboardQueryHandler;
    }

    @Override
    public CustomerFindOneQueryResponse findOneCustomerBy(FindOneCustomerQuery findOneCustomerQuery) {
        return customerFindOneQueryHandler.findOneCustomerBy(findOneCustomerQuery);
    }

    @Override
    public Page<CustomerDashboardQueryResponse> search(CustomerDashboardQuery customerDashboardQuery) {
        return customerDashboardQueryHandler.search(customerDashboardQuery);
    }
}
