package com.example.customerservice.application.dto;

import com.example.modulecommon.domain.constant.SortCriteria;
import jakarta.validation.constraints.NotNull;

public record CustomerSearchCondition(
        String name,
        String street,
        @NotNull SortCriteria sortCriteria
) {

    public static CustomerSearchCondition of(String name, String street, SortCriteria sortCriteria) {
        return new CustomerSearchCondition(name, street, sortCriteria);
    }

}

