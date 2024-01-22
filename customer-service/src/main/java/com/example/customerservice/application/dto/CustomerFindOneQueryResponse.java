package com.example.customerservice.application.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerFindOneQueryResponse (
        @NotNull UUID id) {

    static public CustomerFindOneQueryResponse of(UUID id) {
        return new CustomerFindOneQueryResponse(id);
    }
}
