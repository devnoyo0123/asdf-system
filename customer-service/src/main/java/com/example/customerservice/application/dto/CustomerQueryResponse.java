package com.example.customerservice.application.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerQueryResponse(
        @NotNull UUID id,
        @NotNull String name,
        @NotNull String phone,
        @NotNull String street
) {

    static public CustomerQueryResponse of(UUID id, String name, String phone, String street) {
        return new CustomerQueryResponse(id, name, phone, street);
    }
}
