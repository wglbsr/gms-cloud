package com.dyny.common.packet;

/**
 * @Auther: lane
 * @Date: 2019-03-22 09:49
 * @Description:
 * @Version 1.0.0
 */
public interface IG1TcpPacket {

    IG1WsPacket toWsPacket(IG1TcpPacket g1TcpPacket);


}
