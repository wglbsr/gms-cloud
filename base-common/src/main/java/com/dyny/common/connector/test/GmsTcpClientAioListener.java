package com.dyny.common.connector.test;

import com.dyny.common.connector.packet.GmsTcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioListener;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:24
 * @Description:
 * @Version 1.0.0
 */
public class GmsTcpClientAioListener implements ServerAioListener, ClientAioListener {
    private static Logger logger = LoggerFactory.getLogger(GmsTcpClientAioListener.class);


    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
    }


    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {

    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {

    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        GmsTcpPacket gmsTcpPacket = (GmsTcpPacket) packet;
        logger.info("发送[{}]" + gmsTcpPacket.getFullContent(true));
    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {

    }
}
