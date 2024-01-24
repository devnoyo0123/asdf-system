package com.example.customerservice.adapter.out.dataaccess.entity;

import com.example.modulecommon.domain.valueobject.Address;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_tbl")
@Entity
public class CustomerEntity {

    @Id
    private UUID id;
    private String name;
    @Setter private String phone;

    @Embedded @Column(nullable = true)
    @Setter private Address address;
}
