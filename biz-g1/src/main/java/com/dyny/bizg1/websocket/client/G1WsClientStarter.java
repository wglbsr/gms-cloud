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


    public static ClientGroupContext clientGroupContext;
    public static TioClient tioClient;
    public static ClientChannelContext clientChannelContext = null;
    private static ReconnConf reconnConf = new ReconnConf(5000L);


    public static void start(String ip, int port) throws Exception {
        Node serverNode = new Node(ip, port);
        ClientAioHandler wsClientAioHandler = new G1WsClientAioHandler();
        clientGroupContext = new ClientGroupContext(wsClientAioHandler, new G1WsClientAioListener(), reconnConf);
        tioClient = new TioClient(clientGroupContext);
        clientChannelContext = tioClient.connect(serverNode);
        //必须发送握手包
        WsResponse wsResponse = new WsResponse();
        wsResponse.setBody("handshake".getBytes());
        wsResponse.setWsOpcode(Opcode.TEXT);
        wsResponse.setHandShake(true);
        Tio.send(G1WsClientStarter.clientChannelContext, wsResponse);
    }

    public static void main(String[] args) throws Exception {
        G1WsClientStarter.start("127.0.0.1", 6789);
    }
}
