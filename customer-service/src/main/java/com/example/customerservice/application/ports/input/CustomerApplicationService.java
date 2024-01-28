package com.example.customerservice.application.ports.input;

import com.example.customerservice.application.dto.CustomerDashboardQuery;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.dto.CustomerQueryResponse;
import com.example.customerservice.application.dto.FindOneCustomerQuery;
import org.springframework.data.domain.Page;

public interface CustomerApplicationService {

    CustomerQueryResponse findOneCustomerBy(FindOneCustomerQuery findOneCustomerQuery);

    Page<CustomerDashboardQueryResponse> search(CustomerDashboardQuery customerDashboardQuery);

}
