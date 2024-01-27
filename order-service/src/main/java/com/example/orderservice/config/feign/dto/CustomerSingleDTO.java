package com.example.orderservice.config.feign.dto;

import java.util.UUID;

public record CustomerSingleDTO(
        String id,
        String name,
        String phone,
        String street
) {
}
