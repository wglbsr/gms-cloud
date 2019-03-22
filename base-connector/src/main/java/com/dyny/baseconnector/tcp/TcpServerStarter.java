package com.dyny.baseconnector.tcp;

import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;

/**
 * @Auther: lane
 * @Date: 2018-12-26 13:56
 * @Description:
 * @Version 1.0.0
 */
public class TcpServerStarter {
    public static ServerAioHandler aioHandler = new TcpServerPacketHandler();
    public static ServerAioListener aioListener = null;
    public static ServerGroupContext serverGroupContext = new ServerGroupContext("lms-wardrobe-server", aioHandler, aioListener);
    public static TioServer tioServer = new TioServer(serverGroupContext);
    public static String serverIp = null;
    public static final int serverPort = 6789;

    public static void main(String[] args) throws IOException {
        tioServer.start(serverIp, serverPort);
    }
}
