package com.example.customerservice.application.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerFindOneQueryResponse (
        @NotNull UUID id,
        @NotNull String name,
        @NotNull String phone,
        @NotNull String street
) {

    static public CustomerFindOneQueryResponse of(UUID id, String name, String phone, String street) {
        return new CustomerFindOneQueryResponse(id, name, phone, street);
    }
}
