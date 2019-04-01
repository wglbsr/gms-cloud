package com.dyny.bizg1.websocket.client;

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
 * @Date: 2019-03-20 10:09
 * @Description:
 * @Version 1.0.0
 */
public class G1WsMsgClientHandler implements IWsMsgHandler {
    private Logger logger = LoggerFactory.getLogger(G1WsMsgClientHandler.class);

    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        return httpResponse;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //握手成功,则绑定相关参数（设备id、token）到channel,同一个设备id放在同一个group
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
        return "G1 字节消息[" + Hex.encodeHexString(wsRequest.getBody()) + "]已收到";
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        logger.info("G1 连接关闭");
        return null;
    }

    /**
     * @Author wanggl(lane)
     * @Description //TODO 服务器间主要是使用文本格式
     * @Date 10:15 2019-04-01
     * @Param [wsRequest, text, channelContext]
     * @return java.lang.Object
     **/
    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        String msg = "G1 文本消息[" + text + "]已收到";


        logger.info(msg);
        return msg;
    }


}
