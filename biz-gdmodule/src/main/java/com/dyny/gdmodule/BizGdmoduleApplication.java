package com.dyny.gdmodule;

import com.dyny.gdmodule.websocket.server.MGWsServerStarter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.IOException;
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.dyny.gdmodule.db.dao")
public class BizGdmoduleApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(BizGdmoduleApplication.class, args);
//        MGWsServerStarter.start();

    }

}
