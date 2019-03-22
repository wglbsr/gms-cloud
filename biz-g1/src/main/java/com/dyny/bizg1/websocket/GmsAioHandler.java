package com.dyny.bizg1.websocket;

import com.dyny.common.annotation.Unfinished;
import com.dyny.common.constant.TcpConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.core.maintain.ClientNodes;
import org.tio.http.common.*;
import org.tio.server.intf.ServerAioHandler;
import org.tio.utils.hutool.StrUtil;
import org.tio.websocket.common.*;
import org.tio.websocket.common.util.BASE64Util;
import org.tio.websocket.common.util.SHA1Util;
import org.tio.websocket.server.WsServerConfig;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:53
 * @Description:
 * @Version 1.0.0
 */
public class GmsAioHandler implements ServerAioHandler, ClientAioHandler {
    private static Logger logger = LoggerFactory.getLogger(GmsAioHandler.class);

    public GmsAioHandler(WsServerConfig wsServerConfig, IWsMsgHandler wsMsgHandler) {
        this.wsServerConfig = wsServerConfig;
        this.wsMsgHandler = wsMsgHandler;
    }

    public GmsAioHandler() {

    }

    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        return wsPacketDecoder(buffer, limit, position, readableLength, channelContext);
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return wsEncoder(packet, groupContext, channelContext);
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        wsPacketHandler(packet, channelContext);
    }

    /**
     * @Author wanggl(lane)
     * @Description //TODO 判断是否来自浏览器的连接,非浏览器的连接(即从服务器)将会保存在缓存中
     * @Date 17:34 2019-03-22
     * @Param [channelContext]
     * @return boolean
     **/
    @Unfinished
    public static boolean isFromBs(ChannelContext channelContext) {
        //首先查找attr里面查找KEY_IS_WS_CONNECTION是否为websocket连接
        Boolean isWsConnection = (Boolean) channelContext.getAttribute(TcpConstant.KEY_IS_WS_CONNECTION);
        if (isWsConnection == null) {
            String key = ClientNodes.getKey(channelContext);
            //从缓存中查找
//            RedisApi redisApi = SpringBeanUtils.getBean(RedisApi.class);
//            redisApi.get(TcpConstant.KEY_WS_SERVER_LIST);

            return true;
        } else {
            return isWsConnection;
        }
    }



    /**********************************websocket的相关处理逻辑start******************************************/
    private ByteBuffer wsEncoder(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        WsResponse wsResponse = (WsResponse) packet;
        if (wsResponse.isHandShake()) {
            WsSessionContext imSessionContext = (WsSessionContext) channelContext.getAttribute();
            HttpResponse handshakeResponse = imSessionContext.getHandshakeResponse();
            try {
                return HttpResponseEncoder.encode(handshakeResponse, groupContext, channelContext);
            } catch (UnsupportedEncodingException e) {
                logger.error(e.toString(), e);
                return null;
            }
        }

        ByteBuffer byteBuffer = WsServerEncoder.encode(wsResponse, groupContext, channelContext);
        return byteBuffer;
    }


    private void wsPacketHandler(Packet packet, ChannelContext channelContext) throws Exception {
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
            return;
        }

        WsResponse wsResponse = h(wsRequest, wsRequest.getBody(), wsRequest.getWsOpcode(), channelContext);

        if (wsResponse != null) {
            Tio.send(channelContext, wsResponse);
        }
    }

    private WsServerConfig wsServerConfig;

    private IWsMsgHandler wsMsgHandler;

    /**
     * @param httpConfig the httpConfig to set
     */
    public void setHttpConfig(WsServerConfig httpConfig) {
        this.wsServerConfig = httpConfig;
    }


    /**
     * @return org.tio.core.intf.Packet
     * @Author wanggl(lane)
     * @Description //TODO 处理websocket的连接
     * @Date 13:48 2019-03-22
     * @Param [buffer, limit, position, readableLength, channelContext]
     **/
    private Packet wsPacketDecoder(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
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
        logger.info("server 收到websocket数据包getWsBodyText[" + new String(websocketPacket.getBody()) + "]");
        return websocketPacket;
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

    private WsResponse h(WsRequest websocketPacket, byte[] bytes, Opcode opcode, ChannelContext channelContext) throws Exception {
        WsResponse wsResponse;
        if (opcode == Opcode.TEXT) {
            if (bytes == null || bytes.length == 0) {
                Tio.remove(channelContext, "错误的websocket包，body为空");
                return null;
            }
            String text = new String(bytes, wsServerConfig.getCharset());
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

    private WsResponse processRetObj(Object obj, String methodName, ChannelContext channelContext) throws Exception {
        WsResponse wsResponse;
        if (obj == null) {
            return null;
        } else {
            if (obj instanceof String) {
                String str = (String) obj;
                wsResponse = WsResponse.fromText(str, wsServerConfig.getCharset());
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
