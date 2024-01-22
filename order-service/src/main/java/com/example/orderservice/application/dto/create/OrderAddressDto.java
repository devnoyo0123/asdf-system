package com.example.orderservice.application.dto.create;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record OrderAddressDto(
        @NotNull @Max(value = 50) String street,
        @Max(value = 10) String postalCode) {
}
