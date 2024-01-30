package com.example.restaurantservice.application.ports.input;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RestaurantApplicationIntegrationTest {
    @Autowired
    private RestaurantApplicationService restaurantApplicationService;
    
    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testFindOneBy() {
        // 테스트에 사용될 쿼리 객체 생성
        
//        RestaurantEntity restaurant = new RestaurantEntity();
//        restaurant.setName("Test Restaurant");
//        restaurant.setActive(true);
//
//
//        RestaurantQuery query = new RestaurantQuery(/* 적절한 파라미터 설정 */);
//
//        // 서비스 메소드 호출
//        RestaurantQueryResponse response = restaurantApplicationService.findOneBy(query);
//
//        // 결과 검증
//        assertThat(response).isNotNull();
//        assertThat(response.getId()).isEqualTo(/* 예상되는 ID */);
//        assertThat(response.getName()).isEqualTo(/* 예상되는 이름 */);
        // 기타 필요한 검증 수행
    }
}