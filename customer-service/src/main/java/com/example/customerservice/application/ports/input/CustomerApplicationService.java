package com.example.customerservice.application.ports.input;

import com.example.customerservice.application.dto.CustomerDashboardQuery;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.dto.CustomerFindOneQueryResponse;
import com.example.customerservice.application.dto.FindOneCustomerQuery;
import org.springframework.data.domain.Page;

public interface CustomerApplicationService {

    CustomerFindOneQueryResponse findOneCustomerBy(FindOneCustomerQuery findOneCustomerQuery);

    Page<CustomerDashboardQueryResponse> search(CustomerDashboardQuery customerDashboardQuery);

}
