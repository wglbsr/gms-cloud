package com.dyny.baseconnector.server.tcp.mg;

import lombok.Data;
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
        this.data12 = ByteBuffer.wrap(data12, 0, 12);
        this.id = ByteBuffer.wrap(data12, 0, 4);
        this.data8 = ByteBuffer.wrap(data12, 4, 8);
    }

    private ByteBuffer data12;
    private ByteBuffer id;
    private ByteBuffer data8;
    private static final int DYNAMIC_MSG_VOL1_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL2_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL3_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL4_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL5_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL6_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL7_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL8_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL9_ID = 0x1C006501;
    private static final int DYNAMIC_MSG_VOL10_ID = 0x1C006501;

    private static ByteBuffer getByteVal(ByteBuffer data, int start, int size) {
        byte[] temp = ByteUtils.subArray(data.array(), start, start + size);
        return ByteBuffer.wrap(temp);
    }

    private static boolean getBitVal(Byte b, int index) {
        return (b.intValue() & (0x01 << index)) > 0;
    }

    /********************动态报文1********************/
    public Float getPhaseAVoltage() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL1_ID)) {
            return getByteVal(this.data8, 0, 2).getInt() * 0.01f;
        }
        return null;
    }

    public Float getPhaseBVoltage() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL1_ID)) {
            return getByteVal(this.data8, 2, 2).getInt() * 0.01f;
        }
        return null;
    }


    public Float getPhaseCVoltage() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL1_ID) {
            return getByteVal(this.data8, 4, 2).getInt() * 0.01f;
        }
        return null;
    }


    public Float getFreq() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL1_ID) {
            return getByteVal(this.data8, 6, 2).getInt() * 0.1f;
        }
        return null;
    }


    /********************动态报文2********************/
    public Float getPhaseACurrent() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL2_ID)) {
            return getByteVal(this.data8, 0, 2).getInt() * 0.001f;
        }
        return null;
    }

    public Float getPhaseBCurrent() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL2_ID)) {
            return getByteVal(this.data8, 2, 2).getInt() * 0.001f;
        }
        return null;
    }


    public Float getPhaseCCurrent() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL2_ID) {
            return getByteVal(this.data8, 4, 2).getInt() * 0.001f;
        }
        return null;
    }

    public Integer getBatteryPercent() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL2_ID) {
            return getByteVal(this.data8, 6, 1).getInt();
        }
        return null;
    }


    public Boolean getCityElec() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL2_ID) {
            ByteBuffer temp = getByteVal(this.data8, 7, 1);
            return getBitVal(temp.get(), 2);
        }
        return null;
    }


    public Boolean getIsLoaded() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL2_ID) {
            ByteBuffer temp = getByteVal(this.data8, 7, 1);
            return getBitVal(temp.get(), 3);
        }
        return null;
    }


    /********************动态报文3********************/
    public Float getTotalElec() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL3_ID)) {
            return getByteVal(this.data8, 4, 4).getInt() * 0.01f;
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
        if (this.id.getInt() == (DYNAMIC_MSG_VOL4_ID)) {
            return getByteVal(this.data8, 4, 4).getInt();
        }
        return null;
    }


    /********************动态报文5********************/
    public Integer getLACSID() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL5_ID)) {
            return getByteVal(this.data8, 0, 2).getInt();
        }
        return null;
    }

    public Integer getCELLIDBID() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL5_ID)) {
            return getByteVal(this.data8, 2, 4).getInt();
        }
        return null;
    }


    /********************动态报文6********************/
    public Integer getGeneratingFlag() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL6_ID)) {
            return getByteVal(this.data8, 0, 1).getInt();
        }
        return null;
    }

    /********************动态报文7********************/
    public Float getLongitude() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL7_ID)) {
            return getByteVal(this.data8, 0, 4).getFloat();
        }
        return null;
    }

    public Float getLatitude() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL7_ID)) {
            return getByteVal(this.data8, 4, 4).getFloat();
        }
        return null;
    }


    /********************动态报文8********************/
    public String getLocalTime() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL8_ID)) {
            int year = getByteVal(this.data8, 0, 1).getInt() + 2000;
            int month = getByteVal(this.data8, 1, 1).getInt();
            int day = getByteVal(this.data8, 2, 1).getInt();
            int hour = getByteVal(this.data8, 3, 1).getInt();
            int minute = getByteVal(this.data8, 4, 1).getInt();
            int second = getByteVal(this.data8, 5, 1).getInt();
        }
        return null;
    }

    public Integer getGeneratingSerialNum() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL8_ID)) {
            return getByteVal(this.data8, 6, 2).getInt();
        }
        return null;
    }

    /********************动态报文9********************/
    public String getCCID() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL9_ID)) {
            byte[] data = getByteVal(this.data8, 0, 8).array();
            StringBuilder ccidSB = new StringBuilder();
            ccidSB.append("8986");
            for (int i = 0; i < data.length; i++) {
                ccidSB.append(data[i]);
            }
            return ccidSB.toString();
        }
        return null;
    }

    /********************动态报文10********************/
    public Integer getBatteryVoltage() {
        if (this.id.getInt() == (DYNAMIC_MSG_VOL10_ID)) {
            return getByteVal(this.data8, 2, 2).getInt();
        }
        return null;
    }

    public Boolean getBatVolTooLow() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL10_ID) {
            ByteBuffer temp = getByteVal(this.data8, 7, 1);
            return getBitVal(temp.get(), 2);
        }
        return null;
    }

    public Boolean getDCVolTooHigh() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL10_ID) {
            ByteBuffer temp = getByteVal(this.data8, 7, 1);
            return getBitVal(temp.get(), 4);
        }
        return null;
    }

    public Boolean getDCCurtTooHigh() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL10_ID) {
            ByteBuffer temp = getByteVal(this.data8, 7, 1);
            return getBitVal(temp.get(), 5);
        }
        return null;
    }


    public Boolean getCircuitError() {
        if (this.id.getInt() == DYNAMIC_MSG_VOL10_ID) {
            ByteBuffer temp = getByteVal(this.data8, 7, 1);
            return getBitVal(temp.get(), 6);
        }
        return null;
    }
}
