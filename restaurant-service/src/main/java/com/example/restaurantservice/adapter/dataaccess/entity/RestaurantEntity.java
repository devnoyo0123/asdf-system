package com.example.restaurantservice.adapter.dataaccess.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false)
    @Setter
    private String name;

    @Column(name = "active", nullable = false)
    @Setter
    private boolean active;

    @OneToMany(mappedBy = "restaurant")
    @Setter
    private List<RestaurantProductEntity> restaurantProducts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}