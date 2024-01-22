package com.example.customerservice.application.ports.input;

import com.example.customerservice.application.dto.CustomerFindOneQueryResponse;
import com.example.customerservice.application.dto.FindOneCustomerQuery;

public interface CustomerApplicationService {

    CustomerFindOneQueryResponse findOneCustomerBy(FindOneCustomerQuery findOneCustomerQuery);
}
