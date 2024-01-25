package com.example.customerservice.adapter.out.dataaccess.repository;

import com.example.customerservice.adapter.out.dataaccess.entity.CustomerEntity;
import com.example.customerservice.adapter.out.dataaccess.entity.QCustomerEntity;
import com.example.customerservice.adapter.out.dataaccess.mapper.CustomerDataAccessMapper;
import com.example.customerservice.application.dto.CustomerDashboardQueryResponse;
import com.example.customerservice.application.dto.CustomerSearchCondition;
import com.example.modulecommon.domain.constant.OrderSort;
import com.example.modulecommon.domain.constant.SortCriteria;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;



public class CustomerCustomRepositoryImpl implements CustomerCustomRepository {

    private final JPAQueryFactory queryFactory;

    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerCustomRepositoryImpl(EntityManager em, CustomerDataAccessMapper customerDataAccessMapper) {
        this.queryFactory = new JPAQueryFactory(em);
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Page<CustomerDashboardQueryResponse> search(
            CustomerSearchCondition condition,
            Pageable pageable) {

        OrderSpecifier<?> orderSpecifier = getSearchOrderSpecifier(condition.sortCriteria());

        List<CustomerEntity> queryResults = queryFactory
                .select(QCustomerEntity.customerEntity)
                .from(QCustomerEntity.customerEntity)
                .where(nameEq(condition.name()),
                        streetStartWith(condition.street()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier)
                .fetch();

        Long countResult = queryFactory
                .select(QCustomerEntity.customerEntity.count())
                .from(QCustomerEntity.customerEntity)
                .where(nameEq(condition.name()),
                        streetStartWith(condition.street()))
                .fetchOne();

        long count = countResult != null ? countResult : 0;

        return new PageImpl<>(queryResults.stream()
                .map(customerDataAccessMapper::customerEntityToCustomerDashboardQueryResponse)
                .toList(), pageable, count);
    }

    private BooleanExpression nameEq(String name) {
        return hasText(name) ?  QCustomerEntity.customerEntity.name.eq(name): null;
    }

    private BooleanExpression streetStartWith(String street) {
        return hasText(street) ?  QCustomerEntity.customerEntity.address.street.eq(street): null;
    }

    private OrderSpecifier<?> getSearchOrderSpecifier(SortCriteria sortCriteria) {
        if (sortCriteria.getKey() == null || sortCriteria.getKey().isEmpty()) {
            // 기본 정렬: 여기서는 예시로 name의 오름차순 정렬을 사용
            return QCustomerEntity.customerEntity.name.asc();
        }


        return switch (sortCriteria.getKey()) {
            case "name" -> sortCriteria.getOrderSort() == OrderSort.ASC ?
                    QCustomerEntity.customerEntity.name.asc() :
                    QCustomerEntity.customerEntity.name.desc();
            case "street" -> sortCriteria.getOrderSort() == OrderSort.ASC ?
                    QCustomerEntity.customerEntity.address.street.asc() :
                    QCustomerEntity.customerEntity.address.street.desc();
            // 알 수 없는 key에 대한 처리
            default -> throw new IllegalArgumentException("Invalid sort key");
        };
    }
}
