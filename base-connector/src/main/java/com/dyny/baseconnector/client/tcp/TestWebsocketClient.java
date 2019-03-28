package com.dyny.baseconnector.client.tcp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Auther: lane
 * @Date: 2019-03-28 15:47
 * @Description:
 * @Version 1.0.0
 */
public class TestWebsocketClient extends WebSocketClient {
    public static Log logger = LogFactory.getLog(TestWebsocketClient.class);


    public TestWebsocketClient(String url) throws URISyntaxException {
        super(new URI(url));
    }


    public static void main(String args[]) throws URISyntaxException {
        TestWebsocketClient testWebsocketClient = new TestWebsocketClient("ws://127.0.0.1:7600");
        testWebsocketClient.connect();
        if (testWebsocketClient.isConnecting()) {
            testWebsocketClient.send("99888");
        }
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {
        logger.info("收到数据[" + s + "]");
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("关闭连接");
    }

    @Override
    public void onError(Exception e) {
        logger.info("发生错误" + e.toString());
    }
}
