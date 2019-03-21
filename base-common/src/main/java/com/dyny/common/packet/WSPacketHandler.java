package com.dyny.common.packet;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: lane
 * @Date: 2019-03-20 15:18
 * @Description:
 * @Version 1.0.0
 */
public class WSPacketHandler {
    private static final String KEY_STATUS = "status";
    private static final String KEY_RESULT = "result";
    private static final String KEY_DATA = "data";
    private static final String KEY_CHANNEL_ID = "channelId";

    public static String encode(Object data, int status, boolean result
    ) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_DATA, data);
        jsonObject.put(KEY_RESULT, result);
        jsonObject.put(KEY_STATUS, status);
        return jsonObject.toJSONString();
    }

    public static String refuse() {
        return encode(null, -1, false);
    }

    public static <T> T decode(String packetStr, Class<T> T) {
        JSONObject packetJson = JSONObject.parseObject(packetStr);
        JSONObject dataObj = packetJson.getJSONObject(KEY_DATA);
        boolean result = packetJson.getObject(KEY_RESULT, Boolean.class);
        Integer status = packetJson.getObject(KEY_STATUS, Integer.class);
        if (result && status > 0) {
            return dataObj.toJavaObject(T);
        } else {
            return null;
        }
    }


    public static String decodeString(String packetStr) {
        return decode(packetStr, String.class);
    }
}
