package com.dyny.baseconnector.server;

import com.dyny.common.constant.TcpConstant;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
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
public class GmsWsMsgServerHandler implements IWsMsgHandler {
    private Logger logger = LoggerFactory.getLogger(GmsWsMsgServerHandler.class);
    private static final String PATH_SEPARATOR = "/";

    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        String initPath = httpRequest.getRequestLine().getInitPath();
        String deviceId = getDeviceId(initPath);
        //是否为来自通讯模块的连接
        if (!isConnector(initPath)) {
            String token = getToken(initPath);
            //1.判断token是否合法,在此处判断token会破坏模块功能的单一性,应放在网关处判断,而现在使用的zuul不支持websocket
            //暂时只作非空和长度及组成判断
            if (StringUtils.isEmpty(token) || !token.matches("[a-z0-9]{32}")) {
                return null;
            }
        }
        //2.判断设备id是否合法,这里应该用device而不是具体的设备类型
        return httpResponse;
    }


    private String getToken(String initPath) {
        return initPath.substring(initPath.indexOf(PATH_SEPARATOR) + 1, initPath.lastIndexOf(PATH_SEPARATOR));
    }

    private String getDeviceId(String initPath) {
        return initPath.substring(initPath.lastIndexOf(PATH_SEPARATOR) + 1);
    }


    private boolean isConnector(String initPath) {
        String method = initPath.substring(initPath.indexOf(PATH_SEPARATOR) + 1);
        return TcpConstant.METHOD_CONNECTOR.equals(method);
    }


    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //握手成功,则绑定相关参数（设备id、token）到channel,同一个设备id放在同一个group
        String initPath = httpRequest.getRequestLine().getInitPath();
        String deviceId = getDeviceId(initPath);
        if (isConnector(initPath)) {
            channelContext.setBsId(TcpConstant.PREFIX_BS_ID + deviceId);
            channelContext.setAttribute(TcpConstant.KEY_DEVICE_ID, deviceId);
            channelContext.setAttribute(TcpConstant.KEY_IS_CONNECTOR, true);
//            Tio.bindGroup(channelContext, TcpConstant.KEY_CONNECTOR_GROUP);
            logger.info("来自connector的连接,id为[" + deviceId + "],绑定到" + TcpConstant.KEY_CONNECTOR_GROUP + "的固定组");
        } else {
            String token = getToken(initPath);
            channelContext.setBsId(token);
            channelContext.setAttribute(TcpConstant.KEY_DEVICE_ID, deviceId);
            logger.info("客户端[" + token + "],绑定到设备id为[" + deviceId + "]的组");
//            channelContext.setAttribute(TcpConstant.KEY_IS_CONNECTOR, false);
            Tio.bindGroup(channelContext, deviceId);
        }
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
        return "字节消息[" + Hex.encodeHexString(wsRequest.getBody()) + "]已收到";
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        if (bytes != null && bytes.length > 0) {
            logger.info("收到[" + Hex.encodeHexString(bytes) + "]");
        }
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        return "文本消息[" + wsRequest.getWsBodyText() + "]已收到";
    }
}
