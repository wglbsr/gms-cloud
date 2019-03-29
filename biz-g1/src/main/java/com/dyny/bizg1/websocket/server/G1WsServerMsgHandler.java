package com.dyny.bizg1.websocket.server;

import com.dyny.common.constant.TcpConstant;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

/**
 * @Auther: lane
 * @Date: 2019-03-29 14:21
 * @Description:
 * @Version 1.0.0
 */
public class G1WsServerMsgHandler implements IWsMsgHandler {
    private static Logger logger = LoggerFactory.getLogger(G1WsServerMsgHandler.class);

    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        channelContext.setAttribute(TcpConstant.KEY_IS_BIZ_CHANNEL, true);
        logger.info("连接绑定到业务组");
        logger.info("已经连接上来自[{}]的连接", httpRequest.getClientIp());
    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        logger.info("收到字节消息[" + Hex.encodeHexString(bytes) + "]");
        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        logger.info("收到文本消息[" + text + "]");
        return null;
    }
}
