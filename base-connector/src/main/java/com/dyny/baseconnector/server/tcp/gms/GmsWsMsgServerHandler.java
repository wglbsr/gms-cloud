package com.dyny.baseconnector.server.tcp.gms;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.utils.lock.SetWithLock;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.server.handler.IWsMsgHandler;

/**
 * @Auther: lane
 * @Date: 2019-03-20 10:09
 * @Description:
 * @Version 1.0.0
 */
public class GmsWsMsgServerHandler implements IWsMsgHandler {
    private Logger logger = LoggerFactory.getLogger(GmsWsMsgServerHandler.class);

    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String appName = httpRequest.getBodyString();
        logger.info("与业务端[{}]绑定", appName);
    }

    /**
     * @return java.lang.Object
     * @Author wanggl(lane)
     * @Description //TODO 该方法只用来与通讯模块交互数据,网页端不能用
     * @Date 08:55 2019-03-22
     * @Param [wsRequest, bytes, channelContext]
     **/
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        logger.info("收到字节消息[{}]", Hex.encodeHexString(wsRequest.getBody()));
        return null;

    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        if (bytes != null && bytes.length > 0) {
            logger.info("关闭连接");
        }
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        SetWithLock set =  channelContext.getGroupContext().connections;

        logger.info("文本消息[{}]已收到", wsRequest.getWsBodyText());
        return null;

    }
}
