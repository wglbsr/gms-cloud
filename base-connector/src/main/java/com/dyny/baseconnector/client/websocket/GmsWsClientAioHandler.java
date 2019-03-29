package com.dyny.baseconnector.client.websocket;

import com.dyny.common.connector.handler.CommonHandler;
import com.dyny.common.connector.packet.GmsResWsPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
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
public class GmsWsClientAioHandler implements ClientAioHandler {
    private static Logger logger = LoggerFactory.getLogger(GmsWsClientAioHandler.class);

    private IWsMsgHandler wsMsgHandler = new GmsWsMsgClientHandler();


    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        return WsServerDecoder.decode(buffer, channelContext);
    }


    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return WsServerEncoder.encode((WsResponse) packet, groupContext, channelContext);
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        WsRequest wsRequest = (WsRequest) packet;
        GmsResWsPacket wsResponse = CommonHandler.tcpHandler(wsRequest, wsRequest.getBody(), wsRequest.getWsOpcode(), channelContext, wsMsgHandler);
    }


    @Override
    public Packet heartbeatPacket(ChannelContext channelContext) {
        return null;
    }

}
