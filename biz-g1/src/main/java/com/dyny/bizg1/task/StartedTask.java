package com.dyny.bizg1.task;

import com.dyny.bizg1.websocket.G1WsMsgHandler;
import com.dyny.bizg1.websocket.WsServerStarter;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 启动任务
 */
@Component
@Order(1)
public class StartedTask implements ApplicationRunner {
    //开启ws
    public static WsServerStarter wsServerStarter;


    private void startListener() throws Exception {
        try {
            wsServerStarter = new WsServerStarter(7500, new G1WsMsgHandler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        wsServerStarter.start();
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        startListener();
    }


}
