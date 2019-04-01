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

    /**
     * @return org.tio.http.common.HttpResponse
     * @Author wanggl(lane)
     * @Description //TODO 握手操作
     * @Date 09:16 2019-04-01
     * @Param [httpRequest, httpResponse, channelContext]
     **/
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        return httpResponse;
    }

    /**
     * @return void
     * @Author wanggl(lane)
     * @Description //TODO 成功握手后 (全都为来自浏览器的连接)
     * @Date 09:16 2019-04-01
     * @Param [httpRequest, httpResponse, channelContext]
     **/
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //1.获取设配id;
        String initPath = httpRequest.getRequestLine().getInitPath();
        String deviceId = getDeviceId(initPath);
        channelContext.setAttribute(TcpConstant.KEY_DEVICE_ID, deviceId);
        logger.info("G1 server 已经连接上来自[{}]的连接,设备id[{}]", httpRequest.getClientIp(), deviceId);
    }

    private String getDeviceId(String initPath) {
        return initPath.substring(initPath.lastIndexOf("/") + 1);
    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        logger.info("G1 server 收到字节消息[{}]", Hex.encodeHexString(bytes));
        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        logger.info("G1 server 关闭连接");
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        logger.info("G1 server 收到文本消息[{}]", text);
        return null;
    }
}
