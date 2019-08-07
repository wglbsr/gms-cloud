package com.dyny.baseconnector.server.tcp.gms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.intf.TioUuid;
import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;
import org.tio.utils.Threads;
import org.tio.utils.thread.pool.SynThreadPoolExecutor;
import org.tio.websocket.common.WsTioUuid;
import org.tio.websocket.server.WsServerConfig;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author tanyaowu
 * 2017年7月30日 上午9:45:54
 */
public class GmsTcpServerStarter {
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(org.tio.websocket.server.WsServerStarter.class);

    private WsServerConfig wsServerConfig;

    private IWsMsgHandler wsMsgHandler;

    private ServerAioHandler gmsAioHandler;

    private ServerAioListener gmsAioListener;

    private ServerGroupContext serverGroupContext;

    private TioServer tioServer;

    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public GmsTcpServerStarter(int port, IWsMsgHandler wsMsgHandler) throws IOException {
        this(port, wsMsgHandler, null, null);
    }

    public GmsTcpServerStarter(int port, IWsMsgHandler wsMsgHandler, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor) throws IOException {
        this(new WsServerConfig(port), wsMsgHandler, tioExecutor, groupExecutor);
    }

    public GmsTcpServerStarter(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor) throws IOException {
        this(wsServerConfig, wsMsgHandler, new WsTioUuid(), tioExecutor, groupExecutor);
    }

    public GmsTcpServerStarter(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler, TioUuid tioUuid, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor)
            throws IOException {
        if (tioExecutor == null) {
            tioExecutor = Threads.getTioExecutor();
        }
        if (groupExecutor == null) {
            groupExecutor = Threads.getGroupExecutor();
        }
        this.wsServerConfig = wsServerConfig;
        this.wsMsgHandler = wsMsgHandler;
        gmsAioHandler = new GmsTcpServerAioHandler(wsServerConfig, wsMsgHandler);
        gmsAioListener = new GmsTcpServerAioListener();
        serverGroupContext = new ServerGroupContext("base-connector Server", gmsAioHandler, gmsAioListener, tioExecutor, groupExecutor);
        serverGroupContext.setHeartbeatTimeout(0);
        serverGroupContext.setTioUuid(tioUuid);
        tioServer = new TioServer(serverGroupContext);
    }

    public void start() throws IOException {
        tioServer.start(wsServerConfig.getBindIp(), wsServerConfig.getBindPort());
    }

    public static void main(String[] args) throws IOException {
        GmsTcpServerStarter wsServerStarter = new GmsTcpServerStarter(6789, new GmsWsMsgServerHandler());
        wsServerStarter.start();
    }
}
