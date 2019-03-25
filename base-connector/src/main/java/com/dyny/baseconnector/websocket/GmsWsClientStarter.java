package com.dyny.baseconnector.websocket;

import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.websocket.common.Opcode;
import org.tio.websocket.common.WsResponse;

/**
 * @author tanyaowu
 * 2017年7月30日 上午9:45:54
 */
public class GmsWsClientStarter {
    private ClientChannelContext clientChannelContext = null;
    private GmsWsClientAioHandler wsClientAioHandler;

    public GmsWsClientStarter(String ip, int port) {
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
        wsClientAioHandler = new GmsWsClientAioHandler();
        clientGroupContext = new ClientGroupContext(wsClientAioHandler, null, reconnConf);
        tioClient = new TioClient(clientGroupContext);
        clientChannelContext = tioClient.connect(serverNode);
    }

    public static void main(String[] args) throws Exception {
        GmsWsClientStarter gmsWsClientStarter = new GmsWsClientStarter("127.0.0.1", 7600);
        gmsWsClientStarter.start();
        WsResponse wsResponse = new WsResponse();
        wsResponse.setWsOpcode(Opcode.BINARY);
        wsResponse.setBody("123456".getBytes());
        Tio.send(gmsWsClientStarter.getClientChannelContext(), wsResponse);
    }
}
