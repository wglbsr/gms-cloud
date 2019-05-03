package com.dyny.baseconnector.server.udp;

import org.tio.core.udp.UdpServer;
import org.tio.core.udp.UdpServerConf;

import java.net.SocketException;

/**
 * @Auther: lane
 * @Date: 2019-05-03 15:52
 * @Description:
 * @Version 1.0.0
 */
public class UdpServerStarter {
    public static void main(String[] args) throws SocketException {
        GmsUdpServerHandler GmsUdpServerHandler = new GmsUdpServerHandler();
        UdpServerConf udpServerConf = new UdpServerConf(3000, GmsUdpServerHandler, 5000);
        UdpServer udpServer = new UdpServer(udpServerConf);
        udpServer.start();
    }
}
