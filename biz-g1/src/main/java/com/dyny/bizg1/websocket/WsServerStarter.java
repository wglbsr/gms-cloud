package com.dyny.bizg1.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.intf.TioUuid;
import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
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
public class WsServerStarter {
    @SuppressWarnings("unused")
    private static Logger log = LoggerFactory.getLogger(org.tio.websocket.server.WsServerStarter.class);

    private WsServerConfig wsServerConfig;

    private IWsMsgHandler wsMsgHandler;

    private GmsAioHandler gmsAioHandler;

    private GmsAioListener gmsAioListener;

    private ServerGroupContext serverGroupContext;

    private TioServer tioServer;


    /**
     * @return the serverGroupContext
     */
    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public WsServerStarter(int port, IWsMsgHandler wsMsgHandler) throws IOException {
        this(port, wsMsgHandler, null, null);
    }

    public WsServerStarter(int port, IWsMsgHandler wsMsgHandler, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor) throws IOException {
        this(new WsServerConfig(port), wsMsgHandler, tioExecutor, groupExecutor);
    }

    public WsServerStarter(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler) throws IOException {
        this(wsServerConfig, wsMsgHandler, null, null);
    }

    public WsServerStarter(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor) throws IOException {
        this(wsServerConfig, wsMsgHandler, new WsTioUuid(), tioExecutor, groupExecutor);
    }

    public WsServerStarter(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler, TioUuid tioUuid, SynThreadPoolExecutor tioExecutor, ThreadPoolExecutor groupExecutor)
            throws IOException {
        if (tioExecutor == null) {
            tioExecutor = Threads.getTioExecutor();
        }

        if (groupExecutor == null) {
            groupExecutor = Threads.getGroupExecutor();
        }
        this.wsServerConfig = wsServerConfig;
        this.wsMsgHandler = wsMsgHandler;
        gmsAioHandler = new GmsAioHandler(wsServerConfig, wsMsgHandler);
        gmsAioListener = new GmsAioListener();
        serverGroupContext = new ServerGroupContext("Tio Websocket Server", gmsAioHandler, gmsAioListener, tioExecutor, groupExecutor);
        serverGroupContext.setHeartbeatTimeout(0);
        serverGroupContext.setTioUuid(tioUuid);
        tioServer = new TioServer(serverGroupContext);
    }

    public void start() throws IOException {
        tioServer.start(wsServerConfig.getBindIp(), wsServerConfig.getBindPort());
    }
}
