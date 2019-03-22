package com.dyny.baseconnector.tcp;

import com.dyny.baseconnector.tcp.packet.TcpPacket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioListener;

/**
 * @Auther: lane
 * @Date: 2019-01-03 16:09
 * @Description:
 * @Version 1.0.0
 */
public class TcpServerAioListener implements ServerAioListener {
    public static Log logger = LogFactory.getLog(TcpClientTestStarter.class);


    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
    }

    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
        TcpPacket tcpPacket = (TcpPacket) packet;
        byte[] fullPack = tcpPacket.getFullPack();
        String content = "";
        for (byte b : fullPack) {
            content += (Integer.toHexString(Byte.toUnsignedInt(b)) + " ");
        }
        if (tcpPacket.isHeartbeat()) {
            logger.info("心跳包(" + fullPack.length + "):[" + content.trim() + "]");
        } else {
            logger.info("解码后的数据(" + fullPack.length + "):[" + content.trim() + "]");
        }
    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {

    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
        TcpPacket tcpPacket = (TcpPacket) packet;
        if (tcpPacket.isHeartbeat()) {//不保存心跳包
            return;
        }
    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
        TcpPacket tcpPacket = (TcpPacket) packet;
        if (tcpPacket.isHeartbeat()) {//不保存心跳包
            return;
        }
    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) {
    }


}
