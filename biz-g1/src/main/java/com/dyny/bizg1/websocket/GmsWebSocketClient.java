package com.dyny.bizg1.websocket;

import javax.websocket.*;

/**
 * @Auther: lane
 * @Date: 2019-03-12 11:04
 * @Description:
 * @Version 1.0.0
 */
@ClientEndpoint
public class GmsWebSocketClient {

    @OnOpen
    public void onOpen(Session session) {
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Client onMessage: " + message);
    }

    @OnClose
    public void onClose() {

    }
}
