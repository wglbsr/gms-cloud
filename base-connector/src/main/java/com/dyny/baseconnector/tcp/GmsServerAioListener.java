package com.dyny.baseconnector.tcp;

import com.dyny.baseconnector.api.RedisApi;
import com.dyny.baseconnector.utils.SpringBeanUtils;
import com.dyny.common.constant.TcpConstant;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.core.maintain.ClientNodes;
import org.tio.server.intf.ServerAioListener;
import org.tio.websocket.common.WsSessionContext;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:24
 * @Description:
 * @Version 1.0.0
 */
public class GmsServerAioListener implements ServerAioListener {

    private RedisApi redisApi;

    public GmsServerAioListener() {
        redisApi = SpringBeanUtils.getBean(RedisApi.class);
    }

    @Override
    public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {

        if (isWsConnection(channelContext)) {
            WsSessionContext wsSessionContext = new WsSessionContext();
            channelContext.setAttribute(wsSessionContext);
            channelContext.setAttribute(TcpConstant.KEY_IS_WS_CONNECTION, true);
        } else {
            channelContext.setAttribute(TcpConstant.KEY_IS_WS_CONNECTION, false);
        }

    }

    private boolean isWsConnection(ChannelContext channelContext) {
        //判断ip地址是否来自服务器,从缓存获取
        //ip:port
        String channelIpNPort = ClientNodes.getKey(channelContext);

        return false;
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
