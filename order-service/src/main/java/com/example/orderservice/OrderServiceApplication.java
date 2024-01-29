package com.example.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@EnableJpaRepositories(
		basePackages = {"com.example.orderservice"}
)
@EntityScan(
		basePackages = {"com.example.orderservice", "com.example.modulecommon.dataaccess"}
)
@SpringBootApplication(
		scanBasePackages = {"com.example.orderservice", "com.example.modulecommon"}
)
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
