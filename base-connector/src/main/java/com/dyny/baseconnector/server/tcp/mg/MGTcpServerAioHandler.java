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
        MGTcpPacket mgTcpPacket = (MGTcpPacket) packet;
        byte[] fullPack = mgTcpPacket.getFullPacket();
        int packLen = 0;
        if (fullPack != null) {
            packLen = fullPack.length;
        }
        int allLen = packLen;
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        //设置字节序
        buffer.order(groupContext.getByteOrder());
        //写入消息体
        if (fullPack != null) {
            buffer.put(fullPack);
        }
        return buffer;
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        MGTcpPacket mgTcpPacket = (MGTcpPacket) packet;
        if (mgTcpPacket != null) {
            logger.info("解码成功!");
        }

    }
}
