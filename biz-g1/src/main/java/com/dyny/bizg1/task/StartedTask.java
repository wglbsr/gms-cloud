package com.dyny.bizg1.task;

import com.dyny.bizg1.websocket.client.G1WsClientStarter;
import com.dyny.bizg1.websocket.server.G1WsServerStarter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Auther: lane
 * @Date: 2019-04-01 08:56
 * @Description:
 * @Version 1.0.0
 */
@Component
@Order(1)
public class StartedTask implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        startWsServerAndClient();
    }

    @Value("${spring.application.name}")
    private String appName;

    private void startWsServerAndClient() throws Exception {
        G1WsClientStarter.start("127.0.0.1", 6789, appName);
        G1WsServerStarter.start();
    }

    public static void main(String[] args) throws Exception {
//        G1WsClientStarter.start("127.0.0.1", 6789);
//        G1WsServerStarter.start();
    }

}
