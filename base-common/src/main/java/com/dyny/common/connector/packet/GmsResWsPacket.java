package com.dyny.common.connector.packet;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.tio.websocket.common.Opcode;
import org.tio.websocket.common.WsResponse;

import java.io.UnsupportedEncodingException;

/**
 * @Auther: lane
 * @Date: 2019-03-28 16:37
 * @Description:
 * @Version 1.0.0
 */
public class GmsResWsPacket extends WsResponse {

    public static GmsResWsPacket fromText(String text, String charset) {
        GmsResWsPacket wsResponse = new GmsResWsPacket();
        try {
            wsResponse.setBody(text.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
        }
        wsResponse.setWsOpcode(Opcode.TEXT);
        return wsResponse;
    }

    public static GmsResWsPacket fromText(String text) {
        return GmsResWsPacket.fromText(text, Charsets.UTF_8.name());
    }

    public static GmsResWsPacket fromBytes(byte[] bytes) {
        GmsResWsPacket wsResponse = new GmsResWsPacket();
        wsResponse.setBody(bytes);
        wsResponse.setWsOpcode(Opcode.BINARY);
        return wsResponse;
    }


    @Override
    public void setWsBodyText(String body) {
        if (StringUtils.isNotEmpty(body)) {
            super.setBody(body.getBytes());
            super.setWsBodyText(body);
        }
    }

    @Override
    public void setBody(byte[] body) {
        if (body != null) {
            super.setBody(body);
            super.setWsBodyText(Hex.encodeHexString(body));
        }
    }

}
