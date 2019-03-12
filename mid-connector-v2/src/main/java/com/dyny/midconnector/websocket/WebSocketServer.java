package com.dyny.midconnector.websocket;

import com.dyny.common.annotation.Unfinished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @project: lms
 * @description:
 * @author: wanggl
 * @create: 2018-11-22 21:11
 **/
@Unfinished
@ServerEndpoint("/{systemId}/{deviceId}")
@Component
public class WebSocketServer {
    private Session session;
    private static CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();
    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    @OnOpen
    public void onOpen(@PathParam("deviceId") String deviceId, @PathParam("systemId") String systemId, Session session) {
        //判断systemId和deviceId是否合法，不合法需要关闭连接

        this.session = session;
        webSockets.add(this);
        logger.info("systemId[" + systemId + "],deviceId[" + deviceId + "]的连接,当前连接数:" + webSockets.size());
    }

    @OnClose
    public void onClose() {
        logger.info("关闭连接:" + session.getRequestURI().getPath());
        this.removeSession(session.getId());
        logger.info("当前连接数:" + webSockets.size());
    }


    private void removeSession(String sessionId) {
        for (WebSocketServer temp : webSockets) {
            if (sessionId.equals(temp.session.getId())) {
                webSockets.remove(temp);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("接受信息:" + session.getRequestURI().getPath());
        logger.info("信息内容:" + message);

    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误:" + session.getRequestURI().getPath());
        logger.error(error.getMessage());
    }


}
