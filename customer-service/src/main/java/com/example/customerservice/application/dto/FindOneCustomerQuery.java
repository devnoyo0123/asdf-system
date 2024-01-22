package com.example.customerservice.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FindOneCustomerQuery (@NotNull UUID id) {

    static public FindOneCustomerQuery of(@NotNull UUID id) {
        return new FindOneCustomerQuery(id);
    }
}
