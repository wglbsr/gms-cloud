package com.dyny.baseconnector.server;

import com.dyny.common.annotation.Unfinished;
import com.dyny.common.connector.handler.CommonHandler;
import com.dyny.common.connector.packet.GmsResWsPacket;
import com.dyny.common.connector.packet.GmsTcpPacket;
import com.dyny.common.constant.TcpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.core.maintain.ClientNodes;
import org.tio.server.intf.ServerAioHandler;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsServerEncoder;
import org.tio.websocket.server.WsServerConfig;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.nio.ByteBuffer;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:53
 * @Description:
 * @Version 1.0.0
 */
public class GmsServerAioHandler implements ServerAioHandler {
    private static Logger logger = LoggerFactory.getLogger(GmsServerAioHandler.class);
    private WsServerConfig wsServerConfig;
    private IWsMsgHandler wsMsgHandler;


    public GmsServerAioHandler(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler) {
        this.wsServerConfig = wsServerConfig;
        this.wsMsgHandler = wsMsgHandler;
    }


    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        if (_isWsConnection(channelContext)) {
            return wsDecoder(buffer, limit, position, readableLength, channelContext);
        } else {
            return tcpDecoder(buffer, limit, position, readableLength, channelContext);
        }
    }

    private boolean _isWsConnection(ChannelContext channelContext) {
        return isWsConnection(channelContext);
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        if (_isWsConnection(channelContext)) {
            return wsEncoder(packet, groupContext, channelContext);
        } else {
            return tcpEncoder(packet, groupContext, channelContext);
        }
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        if (_isWsConnection(channelContext)) {
            wsHandler(packet, channelContext);
        } else {
            tcpHandler(packet, channelContext);
        }
    }

    /**
     * @return boolean
     * @Author wanggl(lane)
     * @Description //TODO 判断是否来自服务器的连接,是则认为是websocket连接
     * @Date 09:52 2019-03-29
     * @Param [channelContext]
     **/
    @Unfinished
    public static boolean isWsConnection(ChannelContext channelContext) {
        //首先查找attr里面查找KEY_IS_WS_CONNECTION是否为websocket连接
        Boolean isWsConnection = (Boolean) channelContext.getAttribute(TcpConstant.KEY_IS_WS_CONNECTION);
        if (isWsConnection == null) {
            String key = ClientNodes.getKey(channelContext);
            String ipReg = "127.0.0.1:.*";
            return true;
//            return false;
//            if (key.matches(ipReg)) {
//                return true;
//            } else {
//                return false;
//            }
            //从缓存中查找
//            RedisApi redisApi = SpringBeanUtils.getBean(RedisApi.class);
//            redisApi.get(TcpConstant.KEY_WS_SERVER_LIST);
        } else {
            return isWsConnection;
        }
    }


    /**
     * @return java.nio.ByteBuffer
     * @Author wanggl(lane)
     * @Description //TODO 普通tcp包解码
     * @Date 09:49 2019-03-29
     * @Param [packet, groupContext, channelContext]
     **/
    private ByteBuffer tcpEncoder(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return CommonHandler.tcpEncoder(packet, groupContext, channelContext);
    }


    /**
     * @return java.nio.ByteBuffer
     * @Author wanggl(lane)
     * @Description //TODO websocket包编码
     * @Date 09:50 2019-03-29
     * @Param [packet, groupContext, channelContext]
     **/
    private ByteBuffer wsEncoder(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return WsServerEncoder.encode((WsResponse) packet, groupContext, channelContext);
    }


    /**
     * @return org.tio.core.intf.Packet
     * @Author wanggl(lane)
     * @Description //TODO 普通tcp包编码
     * @Date 09:50 2019-03-29
     * @Param [buffer, limit, position, readableLength, channelContext]
     **/
    private Packet tcpDecoder(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        byte[] headerBytes = CommonHandler.getTcpHeader(buffer, limit, position, readableLength);
        if (GmsTcpPacket.isHeaderMatch(headerBytes)) {
            logger.info("普通包,有格式");
            return CommonHandler.getNormalPacket(buffer, headerBytes, limit, position, readableLength, channelContext);
        }
        return null;
    }

    /**
     * @return org.tio.core.intf.Packet
     * @Author wanggl(lane)
     * @Description //TODO websocket包的解码
     * @Date 09:51 2019-03-29
     * @Param [buffer, limit, position, readableLength, channelContext]
     **/
    private Packet wsDecoder(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        return CommonHandler.wsDecoder(buffer, limit, position, readableLength, channelContext, wsServerConfig);
    }

    /**
     * @return void
     * @Author wanggl(lane)
     * @Description //TODO 普通tcp包解码后的处理
     * @Date 09:50 2019-03-29
     * @Param [packet, channelContext]
     **/
    private void tcpHandler(Packet packet, ChannelContext channelContext) {
        GmsTcpPacket gmsTcpPacket = (GmsTcpPacket) packet;
        logger.info("received full packet[" + gmsTcpPacket.getFullContent(true) + "]");
        Tio.send(channelContext, new GmsTcpPacket(0x00, GmsTcpPacket.CMD_OPEN_ALL, 0x00));
    }

    /**
     * @return void
     * @Author wanggl(lane)
     * @Description //TODO websocket包解码后的处理
     * @Date 09:50 2019-03-29
     * @Param [packet, channelContext]
     **/
    private void wsHandler(Packet packet, ChannelContext channelContext) throws Exception {
        WsRequest wsRequest = (WsRequest) packet;
        //握手
        if (wsRequest.isHandShake()) {
            CommonHandler.handshake(packet, channelContext, wsMsgHandler);
            return;
        }
        GmsResWsPacket wsResponse = CommonHandler.wsHandler(wsRequest, wsRequest.getWsOpcode(), channelContext, wsMsgHandler);
        if (wsResponse != null) {
            Tio.send(channelContext, wsResponse);
        }
    }

}
