package com.dyny.baseconnector.server.tcp.mg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.server.TioServer;
import org.tio.websocket.client.WebSocket;
import org.tio.websocket.client.WsClient;

import java.io.IOException;

/**
 * @Auther: wglbs
 * @Date: 2019/8/14 15:07
 * @Description:
 * @Version 1.0.0
 */
public class MGWSClientStarter {
    private static Logger logger = LoggerFactory.getLogger(MGWSClientStarter.class);
    private static WebSocket webSocket = null;

    public static WebSocket getWsClient() throws Exception {
        if (webSocket == null) {
            synchronized (MGWSClientStarter.class) {
                if (webSocket == null) {
                    init();
                }
            }
        }
        return webSocket;
    }

    private static void init() throws Exception {
        WsClient client = WsClient.create("ws://127.0.0.1:7600/?encoding=text");
        webSocket = client.connect();
        webSocket.addOnMessage(e -> {
            //1.获取data
            //2.获取目标设备编号
            //3.根据编号获取channel
            //4.发送数据

            logger.info(e.data.getWsBodyText());
        });
        webSocket.addOnOpen(e -> {
            logger.info("已经连接上!");
        });
        webSocket.addOnClose(e -> {
            logger.info("连接已经关闭!");
        });
        webSocket.addOnError(e -> {
            logger.info("出现错误!");
        });
    }

    public static void main(String[] args) throws Exception {
        getWsClient().send("test");
    }
}
