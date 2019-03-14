package com.dyny.bizg1.websocket;

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
@ServerEndpoint("/generator/{deviceId}")
@Component
public class GmsWebSocketServer {
    private Session session;
    private static CopyOnWriteArraySet<GmsWebSocketServer> webSockets = new CopyOnWriteArraySet<>();
    private static Logger logger = LoggerFactory.getLogger(GmsWebSocketServer.class);

    @OnOpen
    public void onOpen(@PathParam("deviceId") String deviceId, Session session) {

        this.session = session;
        webSockets.add(this);
        logger.info("deviceId[" + deviceId + "]的连接,当前连接数:" + webSockets.size());
    }

    @OnClose
    public void onClose() {
        logger.info("关闭连接:" + session.getRequestURI().getPath());
        this.removeSession(session.getId());
        logger.info("当前连接数:" + webSockets.size());
    }


    private void removeSession(String sessionId) {
        for (GmsWebSocketServer temp : webSockets) {
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
