package com.example.orderservice.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer mockBooksService() {
        // WireMockServer 인스턴스를 생성하고 필요한 구성을 설정합니다.
        // 구성 코드 추가 (예: 스텁 정의)
        return new WireMockServer(8080);
    }

}