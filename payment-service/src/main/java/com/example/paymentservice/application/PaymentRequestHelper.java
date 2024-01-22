package com.example.paymentservice.application;

import com.example.paymentservice.application.dto.PaymentRequest;
import com.example.paymentservice.application.exception.PaymentApplicationServiceException;
import com.example.paymentservice.application.mapper.PaymentDataMapper;
import com.example.paymentservice.application.ports.output.dataaccess.repository.PaymentRepository;
import com.example.paymentservice.application.ports.output.message.PaymentEventPublisher;
import com.example.paymentservice.domain.PaymentDomainService;
import com.example.paymentservice.domain.entity.Payment;
import com.example.paymentservice.domain.event.PaymentCancelledEvent;
import com.example.paymentservice.domain.event.PaymentCompletedEvent;
import com.example.paymentservice.domain.event.PaymentEvent;
import com.example.paymentservice.domain.event.PaymentFailedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;

    public PaymentRequestHelper(PaymentDomainService paymentDomainService,
                                PaymentDataMapper paymentDataMapper,
                                PaymentRepository paymentRepository) {
        this.paymentDomainService = paymentDomainService;
        this.paymentDataMapper = paymentDataMapper;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received payment complete event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndInitiatePayment(payment, failureMessages);
        paymentRepository.save(payment);
        return paymentEvent;
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest ) {
        log.info("Received payment rollback event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if(paymentResponse.isEmpty()) {
            log.error("Payment with order id: {} could not be found!", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Payment with order id: {} "+
                    paymentRequest.getOrderId() + " could not be found!");
        }
        Payment payment = paymentResponse.get();
        List<String> failureMessages = new ArrayList<>();
        PaymentEvent paymentEvent = paymentDomainService.validateAndCancelPayment(payment,failureMessages);
        paymentRepository.save(payment);
        return paymentEvent;
    }

}
