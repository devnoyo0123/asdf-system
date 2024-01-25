package com.example.customerservice.application.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerDashboardQueryResponse(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull String street,
        @NotNull String detail
        ) {

        public static CustomerDashboardQueryResponse of(UUID id, String name, String street, String detail) {
                return new CustomerDashboardQueryResponse(id, name, street, detail);
        }
}
