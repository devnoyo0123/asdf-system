package com.example.orderservice.domain.valueobject;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;
import java.util.UUID;

public record StreetAddress(UUID id, String street, String postalCode) {
    public StreetAddress(@NotNull UUID id, @NotNull String street,@NotNull @Size(min=5, max=5, message = "우편번호는 5자리입니다.")String postalCode) {
        this.id = id;
        this.street = street.strip();
        this.postalCode = postalCode.strip();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreetAddress that = (StreetAddress) o;
        return Objects.equals(street, that.street) && Objects.equals(postalCode, that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode);
    }
}
