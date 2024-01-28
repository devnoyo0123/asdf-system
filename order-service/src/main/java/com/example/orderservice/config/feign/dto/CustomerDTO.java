package com.example.orderservice.config.feign.dto;

public record CustomerDTO(
        String id,
        String name,
        String phone,
        String street
) {
}
