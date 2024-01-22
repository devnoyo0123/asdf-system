package com.example.modulecommon.domain.valueobject;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class Address {
    private String street;
    private String zipCode;
    private String details;

    protected Address(String street, String zipCode, String details) {
        this.street = street;
        this.zipCode = zipCode;
        this.details = details;
    }

    public static Address of(String street, String zipCode, String details) {
        return new Address(street, zipCode, details);
    }
}