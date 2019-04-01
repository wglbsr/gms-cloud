package com.dyny.bizg1.websocket.client;

import com.dyny.bizg1.websocket.server.G1WsServerStarter;
import com.dyny.common.connector.handler.CommonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsServerDecoder;
import org.tio.websocket.common.WsServerEncoder;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.nio.ByteBuffer;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:53
 * @Description:
 * @Version 1.0.0
 */
public class G1WsClientAioHandler implements ClientAioHandler {
    private static Logger logger = LoggerFactory.getLogger(G1WsClientAioHandler.class);

    private IWsMsgHandler wsMsgHandler = new G1WsMsgClientHandler();


    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        WsRequest WsRequest = (WsRequest) WsServerDecoder.decode(buffer, channelContext);
        return WsRequest;
    }


    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return WsServerEncoder.encode((WsResponse) packet, groupContext, channelContext);
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        WsRequest wsRequest = (WsRequest) packet;
        WsResponse wsResponse = CommonHandler.wsHandler(wsRequest, wsRequest.getWsOpcode(), channelContext, wsMsgHandler);
        Tio.sendToSet(G1WsServerStarter.tioServer.getServerGroupContext(), G1WsServerStarter.tioServer.getServerGroupContext().connections, wsResponse, null);
    }


    @Override
    public Packet heartbeatPacket(ChannelContext channelContext) {
        return null;
    }

}
