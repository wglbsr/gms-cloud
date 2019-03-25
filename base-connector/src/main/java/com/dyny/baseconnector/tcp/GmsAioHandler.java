package com.dyny.baseconnector.tcp;

import com.dyny.baseconnector.tcp.packet.TcpPacket;
import com.dyny.common.annotation.Unfinished;
import com.dyny.common.constant.TcpConstant;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Node;
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

        if (isWsConnection(channelContext)) {
            return wsDecoder(buffer, limit, position, readableLength, channelContext);
        } else {
            return tcpDecoder(buffer, limit, position, readableLength, channelContext);
        }
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        if (isWsConnection(channelContext)) {
            return wsEncoder(packet, groupContext, channelContext);
        } else {
            return tcpEncoder(packet, groupContext, channelContext);
        }
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        if (isWsConnection(channelContext)) {
            wsHandler(packet, channelContext);
        } else {
            tcpHandler(packet, channelContext);
        }
    }


    @Unfinished
    public static boolean isWsConnection(ChannelContext channelContext) {
        //首先查找attr里面查找KEY_IS_WS_CONNECTION是否为websocket连接
        Boolean isWsConnection = (Boolean) channelContext.getAttribute(TcpConstant.KEY_IS_WS_CONNECTION);
        if (isWsConnection == null) {
            String key = ClientNodes.getKey(channelContext);
            String ipReg = "127.0.0.1:.*";
            if (key.matches(ipReg)) {
                logger.info("来自服务器的连接,[" + key + "]");
//                return false;
                return true;
            } else {
                logger.info("来自设备的连接,[" + key + "]");
                return false;
            }
            //从缓存中查找
//            RedisApi redisApi = SpringBeanUtils.getBean(RedisApi.class);
//            redisApi.get(TcpConstant.KEY_WS_SERVER_LIST);
        } else {
            return isWsConnection;
        }
    }


    /**********************************普通tcp的相关处理逻辑start******************************************/
    private ByteBuffer tcpEncoder(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        TcpPacket tcpPacket = (TcpPacket) packet;
        byte[] fullPack = tcpPacket.getFullPack();
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
     * @return com.yniot.lms.socket.packet.TcpPacket
     * @Author wanggl(lane)
     * @Description //TODO 普通解包
     * @Date 10:47 2019-01-14
     * @Param [buffer, headerBytes, limit, position, readableLength, channelContext]
     **/
    private TcpPacket getNormalPacket(ByteBuffer buffer, byte[] headerBytes, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        //提醒：buffer的开始位置并不一定是0，应用需要从buffer.position()开始读取数据
        //收到的数据组不了业务包，则返回null以告诉框架数据不够
        Byte lengthByte = buffer.get();
        Integer fullPackLength = lengthByte.intValue();
        //数据不正确，则抛出AioDecodeException异常
        Node clientNode = channelContext.getClientNode();
        String ip = clientNode.getIp();
        if (fullPackLength < 0) {
            throw new AioDecodeException("fullPack length [" + fullPackLength + "] is not right, domain:" + ip);
        }
        //除去头部和长度后的长度
        int bodyLength = fullPackLength - TcpPacket.LENGTH_HEADER - 1;
        //收到的数据是否足够组包
        // 不够消息体长度(剩下的buffer组不了消息体)
        if (readableLength - bodyLength < 0) {
            logger.info("长度不够");
            return null;
        } else //组包成功
        {
            TcpPacket tcpPacket = null;
            if (fullPackLength > 0) {
                byte address = buffer.get();
                byte cmd = buffer.get();
                byte[] data = new byte[0];
                int dataLength = fullPackLength - TcpPacket.LENGTH_MIN;
                if (dataLength > 0) {
                    data = new byte[dataLength];
                    buffer.get(data);
                }
                byte check = buffer.get();
                try {
                    tcpPacket = TcpPacket.parse(headerBytes, lengthByte, address, cmd, data, check);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return tcpPacket;
        }
    }

    private Packet tcpDecoder(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {

        if (readableLength < 6) {
            return null;
        }
        byte[] headerBytes = new byte[TcpPacket.LENGTH_HEADER];
        //读取消息体的长度
        buffer.get(headerBytes, 0, headerBytes.length);
        logger.info("header[" + Hex.encodeHexString(headerBytes) + "]");
        //分为普通包和二维码包,直接为二维码图片对应的字符,因此也没有头部
        if (TcpPacket.isHeaderMatch(headerBytes)) {
            logger.info("普通包,有格式");
            return this.getNormalPacket(buffer, headerBytes, limit, position, readableLength, channelContext);
        } else {
            return null;
        }
    }

    private void tcpHandler(Packet packet, ChannelContext channelContext) {
        TcpPacket tcpPacket = (TcpPacket) packet;
        String wardrobeId = channelContext.getBsId();
        //是否有绑定业务id,没有则发送获取设备id命令
        //没有柜子id则不做后续操作
        if (StringUtils.isEmpty(wardrobeId)) {
            //1设备id
            logger.info("没有绑定id!");
            return;
        }

        logger.info("full packet[" + tcpPacket.getFullContent(true) + "]");
    }


    /*+++++++++++++++++++++++++++++++++++普通tcp的相关处理逻辑end+++++++++++++++++++++++++++++++++++++++++++++*/

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


    private void wsHandler(Packet packet, ChannelContext channelContext) throws Exception {
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
    private Packet wsDecoder(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
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
