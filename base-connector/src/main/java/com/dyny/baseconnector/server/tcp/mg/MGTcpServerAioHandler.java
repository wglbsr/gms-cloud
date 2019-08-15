package com.dyny.baseconnector.server.tcp.mg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
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
            byte[] prodSerial = {0x11, 0x12, 0x13, 0x14, 0x15, 0x16};
            byte[] frameSerial = {0x21, 0x22};
            byte[] payload = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0x0a, 0x0b, 0x0c};

            MGTcpPacket res = new MGTcpPacket((byte) 0x01, (byte) 0x02, prodSerial, payload, frameSerial);
            Tio.send(channelContext, res);
            logger.info("解码成功!");
        }

    }
}
