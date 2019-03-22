package com.dyny.common.packet;

import com.alibaba.fastjson.JSONObject;
import com.dyny.common.constant.TcpConstant;

/**
 * @Auther: lane
 * @Date: 2019-03-20 15:18
 * @Description:
 * @Version 1.0.0
 */
public class WSPacketHandler {


    public static String encode(Object data, int status, boolean result
    ) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TcpConstant.KEY_DATA, data);
        jsonObject.put(TcpConstant.KEY_RESULT, result);
        jsonObject.put(TcpConstant.KEY_STATUS, status);
        return jsonObject.toJSONString();
    }

    public static String refuse() {
        return encode(null, -1, false);
    }

    public static <T> T decode(String packetStr, Class<T> T) {
        JSONObject packetJson = JSONObject.parseObject(packetStr);
        JSONObject dataObj = packetJson.getJSONObject(TcpConstant.KEY_DATA);
        boolean result = packetJson.getObject(TcpConstant.KEY_RESULT, Boolean.class);
        Integer status = packetJson.getObject(TcpConstant.KEY_STATUS, Integer.class);
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
