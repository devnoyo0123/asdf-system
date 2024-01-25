package com.example.customerservice.application.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public record CustomerDashboardQuery(
        @NotNull CustomerSearchCondition searchCondition,
        @NotNull Pageable pageable
) {

    static public CustomerDashboardQuery of(CustomerSearchCondition searchCondition,Pageable pageable) {
        return new CustomerDashboardQuery(searchCondition, pageable);
    }
}
