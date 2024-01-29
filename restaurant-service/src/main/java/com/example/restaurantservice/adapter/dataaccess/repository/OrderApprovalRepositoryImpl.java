package com.example.restaurantservice.adapter.dataaccess.repository;

import com.example.modulecommon.domain.valueobject.OrderId;
import com.example.modulecommon.domain.valueobject.RestaurantId;
import com.example.restaurantservice.adapter.dataaccess.mapper.RestaurantDataAccessMapper;
import com.example.restaurantservice.application.ports.output.repository.OrderApprovalRepository;
import com.example.restaurantservice.domain.entity.OrderApproval;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public OrderApprovalRepositoryImpl(OrderApprovalJpaRepository orderApprovalJpaRepository,
                                       RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.orderApprovalJpaRepository = orderApprovalJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public OrderApproval persist(OrderApproval orderApproval) {
        return restaurantDataAccessMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }

    @Override
    public Optional<OrderApproval> findOneBy(OrderId orderId, RestaurantId restaurantId) {
        return Optional.empty();
    }

}
