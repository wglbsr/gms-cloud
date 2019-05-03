package com.dyny.common.connector.handler;

import com.dyny.common.connector.packet.GmsTcpPacket;
import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.http.common.*;
import org.tio.utils.hutool.StrUtil;
import org.tio.websocket.common.*;
import org.tio.websocket.common.util.BASE64Util;
import org.tio.websocket.common.util.SHA1Util;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: lane
 * @Date: 2019-03-29 09:20
 * @Description:
 * @Version 1.0.0
 */
public class CommonHandler {
    private static Logger logger = LoggerFactory.getLogger(CommonHandler.class);


    public static byte[] getTcpHeader(ByteBuffer buffer, int limit, int position, int readableLength) {
        //84 07 FF 19 01 00 10 FF FF 27 16
        if (readableLength < GmsTcpPacket.LENGTH_MIN) {
            return null;
        }
        byte[] headerBytes = new byte[GmsTcpPacket.LENGTH_HEADER];
        buffer.get(headerBytes, 0, headerBytes.length);
        return headerBytes;
    }

    public static ByteBuffer tcpEncoder(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        GmsTcpPacket gmsTcpPacket = (GmsTcpPacket) packet;
        byte[] fullPack = gmsTcpPacket.getFullPack();
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

    /**
     * 本方法改编自baseio: https://gitee.com/generallycloud/baseio<br>
     * 感谢开源作者的付出
     *
     * @param request
     * @param channelContext
     * @return
     * @author tanyaowu
     */
    public static HttpResponse updateWebSocketProtocol(HttpRequest request, ChannelContext channelContext) {
        Map<String, String> headers = request.getHeaders();
        String Sec_WebSocket_Key = headers.get(HttpConst.RequestHeaderKey.Sec_WebSocket_Key);
        if (StrUtil.isNotBlank(Sec_WebSocket_Key)) {
            String Sec_WebSocket_Key_Magic = Sec_WebSocket_Key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            byte[] key_array = SHA1Util.SHA1(Sec_WebSocket_Key_Magic);
            String acceptKey = BASE64Util.byteArrayToBase64(key_array);
            HttpResponse httpResponse = new HttpResponse(request);
            httpResponse.setStatus(HttpResponseStatus.C101);
            Map<HeaderName, HeaderValue> respHeaders = new HashMap<>();
            respHeaders.put(HeaderName.Connection, HeaderValue.Connection.Upgrade);
            respHeaders.put(HeaderName.Upgrade, HeaderValue.Upgrade.WebSocket);
            respHeaders.put(HeaderName.Sec_WebSocket_Accept, HeaderValue.from(acceptKey));
            httpResponse.addHeaders(respHeaders);
            return httpResponse;
        }
        return null;
    }

    public static WsResponse wsHandler(WsRequest websocketPacket, Opcode opcode, ChannelContext channelContext, IWsMsgHandler wsMsgHandler) throws Exception {


        WsResponse wsResponse;
        byte[] bytes = websocketPacket.getBody();
        if (opcode == Opcode.TEXT) {
            if (bytes == null || bytes.length == 0) {
                Tio.remove(channelContext, "错误的websocket包，body为空");
                return null;
            }
            String text = new String(bytes, Charsets.UTF_8.name());
            Object retObj = wsMsgHandler.onText(websocketPacket, text, channelContext);
            wsResponse = CommonHandler.processRetObj(retObj, channelContext);
            return wsResponse;
        } else if (opcode == Opcode.BINARY) {
            if (bytes == null || bytes.length == 0) {
                Tio.remove(channelContext, "错误的websocket包，body为空");
                return null;
            }
            Object retObj = wsMsgHandler.onBytes(websocketPacket, bytes, channelContext);
            wsResponse = CommonHandler.processRetObj(retObj, channelContext);
            return wsResponse;
        } else if (opcode == Opcode.PING || opcode == Opcode.PONG) {
            logger.debug("收到" + opcode);
            return null;
        } else if (opcode == Opcode.CLOSE) {
            Object retObj = wsMsgHandler.onClose(websocketPacket, bytes, channelContext);
            wsResponse = CommonHandler.processRetObj(retObj, channelContext);
            return wsResponse;
        } else {
            Tio.remove(channelContext, "错误的websocket包，错误的Opcode");
            return null;
        }
    }


    private static WsResponse processRetObj(Object obj, ChannelContext channelContext) throws Exception {
        WsResponse wsResponse;
        if (obj == null) {
            return null;
        } else {
            if (obj instanceof String) {
                String str = (String) obj;
                wsResponse = WsResponse.fromText(str, Charsets.UTF_8.name());
                return wsResponse;
            } else if (obj instanceof byte[]) {
                wsResponse = WsResponse.fromBytes((byte[]) obj);
                return wsResponse;
            } else if (obj instanceof WsResponse) {
                return (WsResponse) obj;
            } else if (obj instanceof ByteBuffer) {
                byte[] bs = ((ByteBuffer) obj).array();
                wsResponse = WsResponse.fromBytes(bs);
                return wsResponse;
            } else {
                logger.error("不支持的类型!");
                return null;
            }
        }
    }


    public static void handshake(Packet packet, ChannelContext channelContext, IWsMsgHandler wsMsgHandler) throws Exception {
        WsRequest wsRequest = (WsRequest) packet;
        if (wsRequest.isHandShake()) {
            WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
            HttpRequest request = wsSessionContext.getHandshakeRequest();
            HttpResponse httpResponse = wsSessionContext.getHandshakeResponse();
            HttpResponse r = wsMsgHandler.handshake(request, httpResponse, channelContext);
            if (r == null) {
                Tio.remove(channelContext, "业务层不同意握手");
                return;
            }
            wsSessionContext.setHandshakeResponse(r);
            WsResponse wsResponse = new WsResponse();
            wsResponse.setHandShake(true);
            Tio.send(channelContext, wsResponse);
            wsSessionContext.setHandshaked(true);
            wsMsgHandler.onAfterHandshaked(request, httpResponse, channelContext);
        }
    }

    //头 长度 命令          数据位            校验   尾部
    //84  07  ff     19 01 00 10 ff ff        27    16
    /**
     * @return com.yniot.lms.socket.packet.GmsTcpPacket
     * @Author wanggl(lane)
     * @Description //TODO 普通解包
     * @Date 10:47 2019-01-14
     * @Param [buffer, headerBytes, limit, position, readableLength, channelContext]
     **/
    public static GmsTcpPacket getNormalPacket(ByteBuffer buffer, byte[] headerBytes, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        //提醒：buffer的开始位置并不一定是0，应用需要从buffer.position()开始读取数据
        //收到的数据组不了业务包，则返回null以告诉框架数据不够
        Byte lengthByte = buffer.get();
        //这个长度是命令加数据的长度,不包括头部,长度,校验位,尾部4个位置
        Integer bodyLength = lengthByte.intValue();
        //数据不正确，则抛出AioDecodeException异常
        Node clientNode = channelContext.getClientNode();
        String ip = clientNode.getIp();
        if (bodyLength < 0) {
            throw new AioDecodeException("fullPack length [" + bodyLength + "] is not right, domain:" + ip);
        }
        // 不够消息体长度(剩下的buffer组不了消息体)
        if (readableLength - bodyLength < 0) {
            logger.info("长度不够");
            return null;
        } else {//组包成功
            GmsTcpPacket gmsTcpPacket = null;
            if (bodyLength > 0) {
                byte cmd = buffer.get();
                byte[] data = new byte[0];
                int dataLength = bodyLength - 1;
                if (dataLength > 0) {
                    data = new byte[dataLength];
                    buffer.get(data);
                }
                byte check = buffer.get();
                byte tail = buffer.get();
                try {
                    gmsTcpPacket = GmsTcpPacket.parse(headerBytes, lengthByte, cmd, data, check);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            logger.info(gmsTcpPacket.getFullContent(true));
            return gmsTcpPacket;
        }
    }

    public static Packet wsDecoder(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext, HttpConfig wsServerConfig) throws AioDecodeException {
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        if (!wsSessionContext.isHandshaked()) {
            HttpRequest request = HttpRequestDecoder.decode(buffer, limit, position, readableLength, channelContext, wsServerConfig);
            if (request == null) {
                return null;
            }
            HttpResponse httpResponse = updateWebSocketProtocol(request, channelContext);
            if (httpResponse == null) {
                throw new AioDecodeException("http协议升级到websocket协议失败");
            }
            wsSessionContext.setHandshakeRequest(request);
            wsSessionContext.setHandshakeResponse(httpResponse);
            WsRequest wsRequestPacket = new WsRequest();
            wsRequestPacket.setHandShake(true);
            return wsRequestPacket;
        }
        WsRequest websocketPacket = WsServerDecoder.decode(buffer, channelContext);
        logger.info("收到websocket数据包[" + new String(websocketPacket.getBody()) + "]");
        return websocketPacket;
    }
}
