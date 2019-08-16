package com.dyny.gdmodule.websocket.server;

import com.dyny.common.connector.packet.DataRule;
import com.dyny.common.constant.TcpConstant;
import com.dyny.common.utils.GDPayloadUtils;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: lane
 * @Date: 2019-03-29 14:21
 * @Description:
 * @Version 1.0.0
 */
public class MGWsServerMsgHandler implements IWsMsgHandler {
    private static Logger logger = LoggerFactory.getLogger(MGWsServerMsgHandler.class);

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
        DataRule dataRuleVolA = new DataRule(GDPayloadUtils.DYNAMIC_MSG_VOL1_ID,
                GDPayloadUtils.KEY_PHASE_A_VOL,
                0,
                2,
                null,
                0.01f,
                Float.class,
                Integer.class,
                null,
                null);
        DataRule dataRuleVolB = new DataRule(GDPayloadUtils.DYNAMIC_MSG_VOL1_ID,
                GDPayloadUtils.KEY_PHASE_B_VOL,
                2,
                2,
                null,
                0.01f,
                Float.class,
                Integer.class,
                null,
                null);
        DataRule dataRuleVolC = new DataRule(GDPayloadUtils.DYNAMIC_MSG_VOL1_ID,
                GDPayloadUtils.KEY_PHASE_C_VOL,
                4,
                2,
                null,
                0.01f,
                Float.class,
                Integer.class,
                null,
                null);
        DataRule dataRuleCurtA = new DataRule(GDPayloadUtils.DYNAMIC_MSG_CURT_ID,
                GDPayloadUtils.KEY_PHASE_A_CURT,
                0,
                2,
                null,
                0.001f,
                Float.class,
                Integer.class,
                null,
                null);
        DataRule dataRuleCityElec = new DataRule(GDPayloadUtils.DYNAMIC_MSG_CURT_ID,
                GDPayloadUtils.KEY_CITY_ELEC,
                7,
                1,
                1,
                0.001f,
                Boolean.class,
                null,
                null,
                null);
        List<DataRule> dataRuleListVol = new ArrayList<>();
        List<DataRule> dataRuleListCurt = new ArrayList<>();
        dataRuleListVol.add(dataRuleVolA);
        dataRuleListVol.add(dataRuleVolB);
        dataRuleListVol.add(dataRuleVolC);
        dataRuleListCurt.add(dataRuleCurtA);
        dataRuleListCurt.add(dataRuleCityElec);

        Map<Integer, List<DataRule>> dataRulesMap = new HashMap<>();
        dataRulesMap.put(GDPayloadUtils.DYNAMIC_MSG_VOL1_ID, dataRuleListVol);
        dataRulesMap.put(GDPayloadUtils.DYNAMIC_MSG_CURT_ID, dataRuleListCurt);

        Map<String, Object> result = GDPayloadUtils.getVal(bytes, dataRulesMap);
        return "ws response3!";
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        logger.info("G1 server 关闭连接");
        return null;
    }

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        logger.info("G1 server 收到文本消息[{}]", text);
        WsResponse wsResponse = WsResponse.fromText("ws response!!", "utf-8");
        Tio.send(channelContext, wsResponse);
        return "ws response2!";
    }
}
