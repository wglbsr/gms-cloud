package com.dyny.baseconnector.server.tcp.mg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;

public class MGTcpServerStarter {
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(MGTcpServerStarter.class);
    private static TioServer tioServer;
    private static ServerGroupContext serverGroupContext;

    public static void main(String[] args) throws IOException {

        init();
    }

    public static void send(String deviceId, Packet packet) {
        Tio.sendToId(serverGroupContext, deviceId, packet);
    }


    private static void init() throws IOException {
        ServerAioHandler mGTcpServerAioHandler = new MGTcpServerAioHandler();
        ServerAioListener mgTcpServerAioListener = new MGTcpServerAioListener();
        String serverName = "base-connector Tcp Server";
        serverGroupContext = new ServerGroupContext(serverName, mGTcpServerAioHandler, mgTcpServerAioListener, null, null);
        serverGroupContext.setHeartbeatTimeout(0);
        tioServer = new TioServer(serverGroupContext);
        tioServer.start("127.0.0.1", 6700);
    }

    public static ServerGroupContext getTcpServerContext() throws IOException {
        if (serverGroupContext == null) {
            synchronized (MGTcpServerStarter.class) {
                if (serverGroupContext == null) {
                    init();
                }
            }
        }
        return serverGroupContext;
    }

    public static TioServer getTcpServer() throws IOException {
        if (tioServer == null) {
            synchronized (MGTcpServerStarter.class) {
                if (tioServer == null) {
                    init();
                }
            }
        }
        return tioServer;
    }
}
