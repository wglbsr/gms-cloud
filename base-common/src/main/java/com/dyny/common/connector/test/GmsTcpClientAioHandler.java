package com.dyny.common.connector.test;

import com.dyny.common.connector.handler.CommonHandler;
import com.dyny.common.connector.packet.GmsTcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:53
 * @Description:
 * @Version 1.0.0
 */
public class GmsTcpClientAioHandler implements ClientAioHandler {
    private static Logger logger = LoggerFactory.getLogger(GmsTcpClientAioHandler.class);

    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        byte[] headerBytes = CommonHandler.getTcpHeader(buffer, limit, position, readableLength);
        //分为普通包和二维码包,直接为二维码图片对应的字符,因此也没有头部
        if (GmsTcpPacket.isHeaderMatch(headerBytes)) {
            logger.info("普通包,有格式");
            return CommonHandler.getNormalPacket(buffer, headerBytes, limit, position, readableLength, channelContext);
        } else {
            return null;
        }
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return CommonHandler.tcpEncoder(packet, groupContext, channelContext);
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        GmsTcpPacket gmsTcpPacket = (GmsTcpPacket) packet;
        logger.info("full packet[" + gmsTcpPacket.getFullContent(true) + "]");
        Tio.send(channelContext, new GmsTcpPacket(0x00, GmsTcpPacket.CMD_OPEN_ALL, 0x00));
    }


    @Override
    public Packet heartbeatPacket(ChannelContext channelContext) {
        return null;
    }

}
