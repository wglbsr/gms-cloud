package com.dyny.bizg1.websocket.client;

import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.Node;

/**
 * @author tanyaowu
 * 2017年7月30日 上午9:45:54
 */
public class G1WsClientTestStarter {
    private ClientChannelContext clientChannelContext = null;
    private ClientAioHandler wsClientAioHandler;

    public G1WsClientTestStarter(String ip, int port) {
        this.serverNode = new Node(ip, port);
    }

    public ClientChannelContext getClientChannelContext() {
        return clientChannelContext;
    }

    public void setClientChannelContext(ClientChannelContext clientChannelContext) {
        this.clientChannelContext = clientChannelContext;
    }

    public ClientGroupContext getClientGroupContext() {
        return clientGroupContext;
    }

    public void setClientGroupContext(ClientGroupContext clientGroupContext) {
        this.clientGroupContext = clientGroupContext;
    }

    private ClientGroupContext clientGroupContext;
    private Node serverNode;
    private TioClient tioClient;
    private static ReconnConf reconnConf = new ReconnConf(5000L);


    public void start() throws Exception {
        wsClientAioHandler = new G1WsClientAioHandler();
        clientGroupContext = new ClientGroupContext(wsClientAioHandler, new G1WsClientAioListener(), reconnConf);
        tioClient = new TioClient(clientGroupContext);
        clientChannelContext = tioClient.connect(serverNode);
    }

    public static void main(String[] args) throws Exception {
        G1WsClientTestStarter gmsWsClientTestStarter = new G1WsClientTestStarter("127.0.0.1", 7600);
        gmsWsClientTestStarter.start();
    }
}
