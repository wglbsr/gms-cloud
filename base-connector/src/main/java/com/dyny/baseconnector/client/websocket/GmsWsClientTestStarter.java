package com.dyny.baseconnector.client.websocket;

import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;

/**
 * @author tanyaowu
 * 2017年7月30日 上午9:45:54
 */
public class GmsWsClientTestStarter {
    private ClientChannelContext clientChannelContext = null;
    private ClientAioHandler wsClientAioHandler;

    public GmsWsClientTestStarter(String ip, int port) {
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
        clientGroupContext = new ClientGroupContext(wsClientAioHandler, new GmsWsClientAioListener(), reconnConf);
        tioClient = new TioClient(clientGroupContext);
        clientChannelContext = tioClient.connect(serverNode);
    }

    public static void main(String[] args) throws Exception {
        GmsWsClientTestStarter gmsWsClientTestStarter = new GmsWsClientTestStarter("127.0.0.1", 7600);
        gmsWsClientTestStarter.start();
        WsResponse wsResponse = new WsResponse();
//        wsResponse.setWsOpcode(Opcode.BINARY);
        wsResponse.setBody("123456".getBytes());
        Tio.send(gmsWsClientTestStarter.getClientChannelContext(), wsResponse);
    }
}
