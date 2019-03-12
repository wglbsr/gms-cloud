package com.dyny.midconnector.socket.packet;

import org.tio.core.intf.Packet;

/**
 * @Auther: lane
 * @Date: 2019-03-12 08:55
 * @Description:
 * @Version 1.0.0
 */
public class CommonPacket extends Packet {

    public static final int LENGTH_HEADER = 1;
    public static final int LENGTH_LENGTH = 1;
    public static final int LENGTH_TAIL = 1;
    public static final int LENGTH_CHECK = 1;
    public static final int LENGTH_CMD = 1;
    public static final int LENGTH_DATA = 0;

    public static final int INDEX_START_HEADER = 0;
    public static final int INDEX_START_LENGTH = 1;
    public static final int INDEX_START_TAIL = 1;
    public static final int INDEX_START_CHECK = 1;
    public static final int INDEX_START_CMD = 1;
    public static final int INDEX_START_DATA = 0;
    private Byte[] fullPacket;

    public CommonPacket(Byte[] packet) {
        this.fullPacket = packet;

    }


    public String getStrData() {
        return null;
    }

    public Byte[] getByteData() {
        return null;
    }
}
