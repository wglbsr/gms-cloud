package com.dyny.baseconnector.server.tcp.mg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;

/**
 * @Auther: lane
 * @Date: 2018-12-26 13:56
 * @Description:
 * @Version 1.0.0
 */
public class MGTcpClientStarter {
    private static Logger logger = LoggerFactory.getLogger(MGTcpClientStarter.class);

    //服务器节点
    public static Node serverNode = new Node("127.0.0.1", 6700);
    //handler, 包括编码、解码、消息处理
    public static ClientAioHandler tioClientHandler = new MGTcpClientAioHandler();
    //事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
    public static ClientAioListener aioListener = new MGTcpClientAioListener();
    //断链后自动连接的，不想自动连接请设为null
    private static ReconnConf reconnConf = new ReconnConf(5000L);
    //一组连接共用的上下文对象
    public static ClientGroupContext clientGroupContext = new ClientGroupContext(tioClientHandler, aioListener, reconnConf);
    public static TioClient tioClient = null;
    public static ClientChannelContext clientChannelContext = null;

    /**
     * 启动程序入口
     */
    public static void main(String[] args) throws Exception {
        clientGroupContext.setHeartbeatTimeout(30000);
        tioClient = new TioClient(clientGroupContext);
        clientChannelContext = tioClient.connect(serverNode);


        byte[] prodSerial = {0x11, 0x12, 0x13, 0x14, 0x15, 0x16};
        byte[] frameSerial = {0x21, 0x22};
        byte[] payload = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0x0a, 0x0b, 0x0c};

        MGTcpPacket mgTcpPacket = new MGTcpPacket((byte) 0x01, (byte) 0x02, prodSerial, payload, frameSerial);
        Tio.send(clientChannelContext, mgTcpPacket);
    }


}
