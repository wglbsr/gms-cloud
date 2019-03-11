package com.dyny.midconnector.socket;

import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

/**
 * @Auther: lane
 * @Date: 2019-03-08 10:25
 * @Description:
 * @Version 1.0.0
 */
public class CommonServerAioHandler implements ServerAioHandler {
    @Override
    public Packet decode(ByteBuffer byteBuffer, int i, int i1, int i2, ChannelContext channelContext) throws AioDecodeException {
        return null;
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return null;
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {

    }
}
