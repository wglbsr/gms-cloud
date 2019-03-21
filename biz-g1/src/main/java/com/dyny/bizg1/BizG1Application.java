package com.dyny.bizg1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;

@EnableTransactionManagement
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableHystrixDashboard
@MapperScan("com.dyny.bizg1.db.dao")
public class BizG1Application {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(BizG1Application.class, args);

    }

}
