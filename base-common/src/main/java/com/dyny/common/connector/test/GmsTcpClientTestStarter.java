package com.dyny.common.connector.test;

import com.dyny.common.connector.packet.GmsTcpPacket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
 * @Date: 2018-12-26 13:59
 * @Description:
 * @Version 1.0.0
 */
public class GmsTcpClientTestStarter {
    public static Log logger = LogFactory.getLog(GmsTcpClientTestStarter.class);

    //服务器节点
//    public static Node serverNode = new Node("127.0.0.1", 6789);
    //handler, 包括编码、解码、消息处理
    public static Node serverNode = new Node("free.idcfengye.com", 17691);
    //    public static ClientAioHandler tioClientHandler = new HelloClientAioHandler();
    public static ClientAioHandler tioClientHandler = new GmsTcpClientAioHandler();
    //事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
    public static ClientAioListener aioListener = null;
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
        clientGroupContext.setHeartbeatTimeout(5000);
        tioClient = new TioClient(clientGroupContext);
        clientChannelContext = tioClient.connect(serverNode);
        while (true) {
            Thread.sleep(5000);
        }
    }

    private static void sendDeviceId() {
        logger.info("发送设备id");
        GmsTcpPacket packet = new GmsTcpPacket(0x00, 0xff, 0x19, 0x01, 0x00, 0x10);
        Tio.send(clientChannelContext, packet);
    }


//    private static void sendOpenAll() {
//        logger.info("发送打开所有通道的命令");
//        GmsTcpPacket packet = new GmsTcpPacket(0x00, GmsTcpPacket.CMD_OPEN_ALL, 0x00);
//        Tio.send(clientChannelContext, packet);
//    }
//
//    private static void sendPassword() throws DecoderException {
//        logger.info("发送打开所有通道的命令");
//        String psw = "11666666";
//        GmsTcpPacket packet = new GmsTcpPacket(0x00, GmsTcpPacket.CMD_QR_CODE, Hex.decodeHex(psw));
//        Tio.send(clientChannelContext, packet);
//    }
}
