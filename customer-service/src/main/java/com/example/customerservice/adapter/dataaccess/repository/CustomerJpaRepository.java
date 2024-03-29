package com.example.customerservice.adapter.dataaccess.repository;

import com.example.customerservice.adapter.dataaccess.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID>, CustomerCustomRepository {
}
