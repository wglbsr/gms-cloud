package com.dyny.common.packet;

import org.tio.core.intf.Packet;

/**
 * @Auther: lane
 * @Date: 2019-03-22 09:55
 * @Description:
 * @Version 1.0.0
 */
public class G1Packet extends Packet implements IG1WsPacket, IG1TcpPacket {




    @Override
    public IG1WsPacket toWsPacket(IG1TcpPacket g1TcpPacket) {
        return null;
    }

    @Override
    public IG1WsPacket toTcpPacket(IG1WsPacket g1WsPacket) {
        return null;
    }
}
