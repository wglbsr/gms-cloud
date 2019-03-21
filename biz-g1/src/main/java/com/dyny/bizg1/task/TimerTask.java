package com.dyny.bizg1.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.tio.core.Tio;
import org.tio.websocket.common.Opcode;
import org.tio.websocket.common.WsResponse;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class TimerTask {


    @Scheduled(cron = "0/10 * * * * ? ")
    public void test() {
        String nowStr = LocalDateTime.now().toString();


        WsResponse wsResponse = new WsResponse();
        wsResponse.setWsOpcode(Opcode.BINARY);
        Tio.sendToGroup(StartedTask.wsServerStarter.getServerGroupContext(), "18080008", WsResponse.fromText(nowStr, "UTF-8"));
    }


}
