package com.dyny.common.enums;

/**
 * @Auther: lane
 * @Date: 2019-04-01 09:22
 * @Description:
 * @Version 1.0.0
 */
public enum ConnectionTypeEnum {
    WS_FROM_BS(10, "浏览器的websocket连接"),
    WS_FROM_SERVER(20, "服务器的websocket连接"),
    TCP_FROM_DEVICE(30, "设备的普通tco连接"),
    TCP_FROM_SERVER(40, "服务器的普通tco连接");

    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    public static String KEY_CONNECTION_TYPE = "ConnectionType";

    private ConnectionTypeEnum(Integer state, String name) {
        this.type = state;
        this.name = name;
    }

}
