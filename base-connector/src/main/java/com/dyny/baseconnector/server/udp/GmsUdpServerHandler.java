package com.dyny.baseconnector.server.udp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Node;
import org.tio.core.udp.UdpPacket;
import org.tio.core.udp.intf.UdpHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @Auther: lane
 * @Date: 2019-05-03 15:51
 * @Description:
 * @Version 1.0.0
 */
public class GmsUdpServerHandler implements UdpHandler {
    private static Logger logger = LoggerFactory.getLogger(GmsUdpServerHandler.class);

    @Override
    public void handler(UdpPacket udpPacket, DatagramSocket datagramSocket) {
        byte[] data = udpPacket.getData();
        String msg = new String(data);
        Node remote = udpPacket.getRemote();
        logger.info("收到来自{}的消息:【{}】", remote, msg);
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, new InetSocketAddress(remote.getIp(), remote.getPort()));
        try {
            datagramSocket.send(datagramPacket);
        } catch (Throwable e) {
            logger.error(e.toString(), e);
        }
    }
}
