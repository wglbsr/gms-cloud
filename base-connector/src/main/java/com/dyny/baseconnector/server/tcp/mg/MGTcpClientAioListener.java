package com.dyny.baseconnector.server.tcp.mg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @Auther: lane
 * @Date: 2019-01-22 08:58
 * @Description:
 * @Version 1.0.0
 */
public class MGTcpClientAioListener implements ClientAioListener {
    private static Logger logger = LoggerFactory.getLogger(MGTcpClientAioListener.class);


    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
    }

    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
        logger.info("解码完成,长度[" + packetSize + "]");
    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
        logger.info("接收数据,长度[" + receivedBytes + "]");
    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        logger.info("发送packet,[" + (isSentSuccess ? "成功!" : "失败!") + "]");
    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
//        GmsPacket gmsPacket = (GmsPacket) packet;
//        Tio.send(channelContext, gmsPacket);
    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {

    }
}
