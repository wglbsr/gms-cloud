package com.dyny.baseconnector.server.tcp.mg;

import com.dyny.common.utils.Utils;
import com.google.common.primitives.UnsignedBytes;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioListener;


/**
 * @Auther: wglbs
 * @Date: 2019/8/7 09:18
 * @Description:
 * @Version 1.0.0
 */
public class MGTcpServerAioListener implements ServerAioListener, ClientAioListener {
    private static Logger logger = LoggerFactory.getLogger(MGTcpServerAioListener.class);

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        logger.info("已经连接上!");
    }

    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
        if (packet != null) {
            MGTcpPacket mgTcpPacket = (MGTcpPacket) packet;
            logger.info("解码完成!长度:{},内容:[{}]", packetSize,  Utils.Byte.bytesToString(mgTcpPacket.getFullPacket()));
        }
    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
        logger.info("已经连接上!");

    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        if (isSentSuccess) {
            MGTcpPacket mgTcpPacket = (MGTcpPacket) packet;
            logger.info("发送成功![{}]",  Utils.Byte.bytesToString(mgTcpPacket.getFullPacket()));
        }

    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
        logger.info("已经处理!");

    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
        logger.info("准备关闭连接!");

    }

    @Override
    public boolean onHeartbeatTimeout(ChannelContext channelContext, Long aLong, int i) {
        return false;
    }
}
