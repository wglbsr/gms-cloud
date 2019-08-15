package com.dyny.baseconnector.server.tcp.mg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.websocket.client.WebSocket;
import org.tio.websocket.client.WsClient;

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
        WsClient client = WsClient.create("ws://127.0.0.1:6800/?encoding=text");
        webSocket = client.connect();
        webSocket.addOnMessage(e -> {
            //1.获取data
            //2.获取目标设备编号
            //3.根据编号获取channel
            //4.发送数据
            byte[] prodSerial = {0x11, 0x12, 0x13, 0x14, 0x15, 0x16};
            byte[] frameSerial = {0x21, 0x22};
            byte[] payload = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0x0a, 0x0b, 0x0c};

            MGTcpPacket mgTcpPacket = new MGTcpPacket((byte) 0x02, (byte) 0x02, prodSerial, payload, frameSerial);
            MGTcpServerStarter.send2BsId("1993", mgTcpPacket);
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
