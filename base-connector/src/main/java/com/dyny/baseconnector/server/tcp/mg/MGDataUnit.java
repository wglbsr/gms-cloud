package com.dyny.baseconnector.server.tcp.mg;

import lombok.Data;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.nio.ByteBuffer;

/**
 * @Auther: wglbs
 * @Date: 2019/8/7 11:31
 * @Description:
 * @Version 1.0.0
 */
@Data
public class MGDataUnit {
    public MGDataUnit(byte[] data12) {
        this.data12 = ByteUtils.subArray(data12, 0, 12);
        this.id = ByteUtils.subArray(data12, 0, 4);
        this.data8 = ByteUtils.subArray(data12, 4, 12);
    }


    private byte[] data12;
    private byte[] id;
    private byte[] data8;
    private static final int DYNAMIC_MSG_VOL1_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL2_ID = 0x1C006502;
    private static final int DYNAMIC_MSG_VOL3_ID = 0x1C006504;
    private static final int DYNAMIC_MSG_VOL4_ID = 0x1C006505;
    private static final int DYNAMIC_MSG_VOL5_ID = 0x1C00650B;
    private static final int DYNAMIC_MSG_VOL6_ID = 0x1C00651B;
    private static final int DYNAMIC_MSG_VOL7_ID = 0x1C00651A;
    private static final int DYNAMIC_MSG_VOL8_ID = 0x1C00651C;
    private static final int DYNAMIC_MSG_VOL9_ID = 0x1C00651D;
    private static final int DYNAMIC_MSG_VOL10_ID = 0x1C00651E;

    private static final int STATISTIC_MSG_VOL1_ID = 0x1C006506;
    private static final int STATISTIC_MSG_VOL2_ID = 0x1C006507;
    private static final int STATISTIC_MSG_VOL3_ID = 0x1C006508;
    private static final int STATISTIC_MSG_VOL4_ID = 0x1C006509;
    private static final int STATISTIC_MSG_VOL5_ID = 0x1C00650A;


    private static ByteBuffer getByteVal(byte[] data, int start, int size) {
        byte[] temp = ByteUtils.subArray(data, start, start + size);
        return ByteBuffer.wrap(temp);
    }


    private static boolean getBitVal(Byte b, int index) {
        if (index > 7) {
            index = 7;
        } else if (index < 0) {
            index = 0;
        }
        return (b.intValue() & (0x01 << index)) > 0;
    }

    private static boolean getBitVal(byte[] data, int byteIndex, int bitIndex) {
        return getBitVal(getByteVal(data, byteIndex, 1).get(), bitIndex);
    }

    /********************动态报文1********************/
    public Float getPhaseAVoltage() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL1_ID)) {
            return getByteVal(this.data8, 0, 2).getInt() * 0.01f;
        }
        return null;
    }

    public Float getPhaseBVoltage() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL1_ID)) {
            return ByteBuffer.wrap(this.data8, 2, 2).getInt() * 0.01f;
        }
        return null;
    }


    public Float getPhaseCVoltage() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL1_ID) {
            return ByteBuffer.wrap(this.data8, 4, 2).getInt() * 0.01f;
        }
        return null;
    }


    public Float getFreq() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL1_ID) {
            return ByteBuffer.wrap(this.data8, 6, 2).getInt() * 0.1f;
        }
        return null;
    }


    /********************动态报文2********************/
    public Float getPhaseACurrent() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL2_ID)) {
            return ByteBuffer.wrap(this.data8, 0, 2).getInt() * 0.001f;
        }
        return null;
    }

    public Float getPhaseBCurrent() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL2_ID)) {
            return ByteBuffer.wrap(this.data8, 2, 2).getInt() * 0.001f;
        }
        return null;
    }


    public Float getPhaseCCurrent() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL2_ID) {
            return ByteBuffer.wrap(this.data8, 4, 2).getInt() * 0.001f;
        }
        return null;
    }

    public Integer getBatteryPercent() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL2_ID) {
            return ByteBuffer.wrap(this.data8, 6, 1).getInt();
        }
        return null;
    }


    public Boolean getCityElec() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL2_ID) {
            return getBitVal(this.data8, 7, 2);
        }
        return null;
    }


    public Boolean getIsLoaded() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL2_ID) {
            return getBitVal(this.data8, 7, 3);
        }
        return null;
    }


    /********************动态报文3********************/
    public Float getTotalElec() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL3_ID)) {
            return ByteBuffer.wrap(this.data8, 4, 4).getInt() * 0.01f;
        }
        return null;
    }

    /********************动态报文4********************/
    /**
     * @return java.lang.Integer
     * @Author wanggl(lane)
     * @Description //TODO 有机运行时长
     * @Date 16:58 2019/8/7
     * @Param []
     **/
    public Integer getTotalRunTime() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL4_ID)) {
            return ByteBuffer.wrap(this.data8, 4, 4).getInt();
        }
        return null;
    }


    /********************动态报文5********************/
    public Integer getLACSID() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL5_ID)) {
            return ByteBuffer.wrap(this.data8, 0, 2).getInt();
        }
        return null;
    }

    public Integer getCELLIDBID() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL5_ID)) {
            return ByteBuffer.wrap(this.data8, 2, 4).getInt();
        }
        return null;
    }


    /********************动态报文6********************/
    public Integer getGeneratingFlag() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL6_ID)) {
            return ByteBuffer.wrap(this.data8, 0, 1).getInt();
        }
        return null;
    }

    /********************动态报文7********************/
    public Float getLongitude() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL7_ID)) {
            return ByteBuffer.wrap(this.data8, 0, 4).getFloat();
        }
        return null;
    }

    public Float getLatitude() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL7_ID)) {
            return ByteBuffer.wrap(this.data8, 4, 4).getFloat();
        }
        return null;
    }


    /********************动态报文8********************/
    public String getLocalTime() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL8_ID)) {
            int year = ByteBuffer.wrap(this.data8, 0, 1).getInt() + 2000;
            int month = ByteBuffer.wrap(this.data8, 1, 1).getInt();
            int day = ByteBuffer.wrap(this.data8, 2, 1).getInt();
            int hour = ByteBuffer.wrap(this.data8, 3, 1).getInt();
            int minute = ByteBuffer.wrap(this.data8, 4, 1).getInt();
            int second = ByteBuffer.wrap(this.data8, 5, 1).getInt();
        }
        return null;
    }

    public Integer getGeneratingSerialNum() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL8_ID)) {
            return ByteBuffer.wrap(this.data8, 6, 2).getInt();
        }
        return null;
    }

    /********************动态报文9********************/
    public String getCCID() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL9_ID)) {
            byte[] data = ByteBuffer.wrap(this.data8, 0, 8).array();
            StringBuilder ccidSB = new StringBuilder();
            ccidSB.append("8986");
            ccidSB.append(HexUtils.toHexString(data));
            return ccidSB.toString();
        }
        return null;
    }

    /********************动态报文10********************/
    public Integer getBatteryVoltage() {
        if (ByteBuffer.wrap(this.id).getInt() == (DYNAMIC_MSG_VOL10_ID)) {
            return ByteBuffer.wrap(this.data8, 2, 2).getInt();
        }
        return null;
    }

    public Boolean getBatVolTooLow() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL10_ID) {
            return getBitVal(this.data8, 7, 2);
        }
        return null;
    }

    public Boolean getDCVolTooHigh() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL10_ID) {
            return getBitVal(this.data8, 7, 4);
        }
        return null;
    }

    public Boolean getDCCurtTooHigh() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL10_ID) {
            return getBitVal(this.data8, 7, 5);
        }
        return null;
    }


    public Boolean getCircuitError() {
        if (ByteBuffer.wrap(this.id).getInt() == DYNAMIC_MSG_VOL10_ID) {
            return getBitVal(this.data8, 7, 6);
        }
        return null;
    }
}
