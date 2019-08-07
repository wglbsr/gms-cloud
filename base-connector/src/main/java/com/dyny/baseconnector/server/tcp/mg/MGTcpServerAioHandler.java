package com.dyny.baseconnector.server.tcp.mg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

/**
 * @Auther: wglbs
 * @Date: 2019/8/7 09:17
 * @Description:
 * @Version 1.0.0
 */
public class MGTcpServerAioHandler implements ServerAioHandler {
    private static Logger logger = LoggerFactory.getLogger(MGTcpServerAioHandler.class);


    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        return MGTcpPacket.decodeMGTcpPacket(buffer, limit, position, readableLength, channelContext);
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return null;
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {

    }
}
