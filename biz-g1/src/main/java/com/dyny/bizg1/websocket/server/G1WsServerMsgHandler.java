package com.dyny.bizg1.websocket.server;

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
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        return null;
    }

    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {

    }

    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        return null;
    }
}
