package com.dyny.midconnector.socket;

import com.dyny.midconnector.socket.packet.CommonPacket;
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
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        if (readableLength < 6) {
            return null;
        }
        //头部
        byte[] headerBytes = new byte[CommonPacket.LENGTH_HEADER];
        //读取消息体的长度

        buffer.get(headerBytes, 0, headerBytes.length);


        return null;
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return null;
    }

    /**
     * @return void
     * @Author wanggl(lane)
     * @Description //TODO 根据协议类型创建不同的groupContext
     * @Date 09:49 2019-03-12
     * @Param [packet, channelContext]
     **/
    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {


    }
}
