package com.dyny.bizg1.websocket;

import com.dyny.common.constant.TcpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioListener;
import org.tio.websocket.common.WsSessionContext;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:24
 * @Description:
 * @Version 1.0.0
 */
public class GmsAioListener implements ServerAioListener, ClientAioListener {
    private static Logger logger = LoggerFactory.getLogger(GmsAioListener.class);


    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
        if (isConnected) {
            if (GmsAioHandler.isFromBs(channelContext)) {
                WsSessionContext wsSessionContext = new WsSessionContext();
                wsSessionContext.setHandshaked(true);
                channelContext.setAttribute(wsSessionContext);
                channelContext.setAttribute(TcpConstant.KEY_IS_WS_CONNECTION, true);
            }
        }
    }


    @Override
    public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {

    }

    @Override
    public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {

    }

    @Override
    public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {

    }

    @Override
    public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
    }

    @Override
    public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {

    }
}
