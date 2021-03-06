package com.dyny.bizg1.websocket.server;

import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import org.tio.utils.Threads;
import org.tio.websocket.common.WsTioUuid;
import org.tio.websocket.server.WsServerAioHandler;
import org.tio.websocket.server.WsServerAioListener;
import org.tio.websocket.server.WsServerConfig;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.IOException;

/**
 * @Auther: lane
 * @Date: 2019-03-29 14:24
 * @Description:
 * @Version 1.0.0
 */
public class G1WsServerStarter {
    public static TioServer tioServer;

    public static void start() throws IOException {
        WsServerConfig wsServerConfig = new WsServerConfig(7600);
        IWsMsgHandler wsMsgHandler = new G1WsServerMsgHandler();
        ServerAioHandler serverAioHandler = new WsServerAioHandler(wsServerConfig, wsMsgHandler);
        ServerAioListener serverAioListener = new WsServerAioListener();
        String serverName = "base-connector Websocket Server";
        ServerGroupContext serverGroupContext = new ServerGroupContext(serverName, serverAioHandler,
                serverAioListener, Threads.getTioExecutor(), Threads.getGroupExecutor());
        serverGroupContext.setHeartbeatTimeout(0);
        serverGroupContext.setTioUuid(new WsTioUuid());
        G1WsServerStarter.tioServer = new TioServer(serverGroupContext);
        tioServer.start(wsServerConfig.getBindIp(), wsServerConfig.getBindPort());
    }

    public static void main(String[] args) throws IOException {

    }
}
