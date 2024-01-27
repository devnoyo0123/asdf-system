package com.example.orderservice.application.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderAddressDto(
        @NotNull @Size(max = 50) String street,
        @NotNull @Size(min = 5, max = 5) String postalCode) {
}
