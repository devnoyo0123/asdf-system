package com.example.paymentservice.application.ports.output.dataaccess.repository;


import com.example.paymentservice.domain.entity.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(UUID orderId);
}
