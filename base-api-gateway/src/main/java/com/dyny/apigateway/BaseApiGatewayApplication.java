package com.dyny.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class BaseApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApiGatewayApplication.class, args);
    }
}
