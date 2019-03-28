package com.dyny.baseconnector.client.websocket;

import com.dyny.baseconnector.server.WsMsgServerHandler;
import com.dyny.common.packet.GmsResWsPacket;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.websocket.common.*;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.nio.ByteBuffer;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:53
 * @Description:
 * @Version 1.0.0
 */
public class GmsWsClientAioHandler implements ClientAioHandler {
    private static Logger logger = LoggerFactory.getLogger(GmsWsClientAioHandler.class);

    public GmsWsClientAioHandler() {
    }


    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        return wsDecoder(buffer, limit, position, readableLength, channelContext);
    }


    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return wsEncoder(packet, groupContext, channelContext);
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        wsHandler(packet, channelContext);
    }

    private ByteBuffer wsEncoder(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        WsResponse wsResponse = (WsResponse) packet;
        ByteBuffer byteBuffer = WsServerEncoder.encode(wsResponse, groupContext, channelContext);
        return byteBuffer;
    }


    private void wsHandler(Packet packet, ChannelContext channelContext) throws Exception {
        WsRequest wsRequest = (WsRequest) packet;
        GmsResWsPacket wsResponse = h(wsRequest, wsRequest.getBody(), wsRequest.getWsOpcode(), channelContext);
        if (wsResponse != null) {
            logger.info("收到来自服务系的消息[" + wsResponse.getWsBodyText() + "]");
        } else {
            logger.error("解包失败!");
        }
    }

    private IWsMsgHandler wsMsgHandler = new WsMsgServerHandler();


    /**
     * @return org.tio.core.intf.Packet
     * @Author wanggl(lane)
     * @Description //TODO 处理websocket的连接
     * @Date 13:48 2019-03-22
     * @Param [buffer, limit, position, readableLength, channelContext]
     **/
    private Packet wsDecoder(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        WsRequest websocketPacket = WsServerDecoder.decode(buffer, channelContext);
        return websocketPacket;
    }


    private GmsResWsPacket h(WsRequest websocketPacket, byte[] bytes, Opcode opcode, ChannelContext channelContext) throws Exception {
        GmsResWsPacket wsResponse;
        if (opcode == Opcode.TEXT) {
            if (bytes == null || bytes.length == 0) {
                Tio.remove(channelContext, "错误的websocket包，body为空");
                return null;
            }
            String text = new String(bytes, Charsets.UTF_8.name());
            Object retObj = wsMsgHandler.onText(websocketPacket, text, channelContext);
            String methodName = "onText";
            wsResponse = processRetObj(retObj, methodName, channelContext);
            return wsResponse;
        } else if (opcode == Opcode.BINARY) {
            if (bytes == null || bytes.length == 0) {
                Tio.remove(channelContext, "错误的websocket包，body为空");
                return null;
            }
            Object retObj = wsMsgHandler.onBytes(websocketPacket, bytes, channelContext);
            String methodName = "onBytes";
            wsResponse = processRetObj(retObj, methodName, channelContext);
            return wsResponse;
        } else if (opcode == Opcode.PING || opcode == Opcode.PONG) {
            logger.debug("收到" + opcode);
            return null;
        } else if (opcode == Opcode.CLOSE) {
            Object retObj = wsMsgHandler.onClose(websocketPacket, bytes, channelContext);
            String methodName = "onClose";
            wsResponse = processRetObj(retObj, methodName, channelContext);
            return wsResponse;
        } else {
            Tio.remove(channelContext, "错误的websocket包，错误的Opcode");
            return null;
        }
    }

    private GmsResWsPacket processRetObj(Object obj, String methodName, ChannelContext channelContext) throws Exception {
        GmsResWsPacket wsResponse;
        if (obj == null) {
            return null;
        } else {
            if (obj instanceof String) {
                String str = (String) obj;
                wsResponse = GmsResWsPacket.fromText(str);
                return wsResponse;
            } else if (obj instanceof byte[]) {
                wsResponse = GmsResWsPacket.fromBytes((byte[]) obj);
                return wsResponse;
            } else if (obj instanceof GmsResWsPacket) {
                return (GmsResWsPacket) obj;
            } else if (obj instanceof ByteBuffer) {
                byte[] bs = ((ByteBuffer) obj).array();
                wsResponse = GmsResWsPacket.fromBytes(bs);
                return wsResponse;
            } else {
                logger.error("{} {}.{}()方法，只允许返回byte[]、ByteBuffer、WsResponse或null，但是程序返回了{}", channelContext, this.getClass().getName(), methodName, obj.getClass().getName());
                return null;
            }
        }
    }

    @Override
    public Packet heartbeatPacket(ChannelContext channelContext) {
        return null;
    }
    /*+++++++++++++++++++++++++++++++++++++websocket的相关处理逻辑end+++++++++++++++++++++++++++++++++++++*/

}
