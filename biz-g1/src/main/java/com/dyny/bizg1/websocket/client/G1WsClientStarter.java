package com.dyny.bizg1.websocket.client;

import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.websocket.common.Opcode;
import org.tio.websocket.common.WsResponse;

/**
 * @author tanyaowu
 * 2017年7月30日 上午9:45:54
 */
public class G1WsClientStarter {
    private ClientAioHandler wsClientAioHandler;
    private Node serverNode;

    public G1WsClientStarter(String ip, int port) {
        this.serverNode = new Node(ip, port);
    }

    public static ClientGroupContext clientGroupContext;
    public static TioClient tioClient;
    public static ClientChannelContext clientChannelContext = null;
    private static ReconnConf reconnConf = new ReconnConf(5000L);


    public void start() throws Exception {
        wsClientAioHandler = new G1WsClientAioHandler();
        clientGroupContext = new ClientGroupContext(wsClientAioHandler, new G1WsClientAioListener(), reconnConf);
        tioClient = new TioClient(clientGroupContext);
        clientChannelContext = tioClient.connect(serverNode);
    }

    public static void main(String[] args) throws Exception {
        G1WsClientStarter gmsWsClientTestStarter = new G1WsClientStarter("127.0.0.1", 6789);
        gmsWsClientTestStarter.start();
        WsResponse wsResponse = new WsResponse();
        wsResponse.setBody("123123".getBytes());
        wsResponse.setWsOpcode(Opcode.TEXT);
        wsResponse.setHandShake(true);
        Tio.send(G1WsClientStarter.clientChannelContext, wsResponse);
    }
}
