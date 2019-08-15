package com.dyny.common.connector.packet;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.nio.ByteBuffer;
import java.util.Map;

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

    private static final String KEY_ID = "id";
    private static final String KEY_LENGTH = "length";

    public <T> T getTargetVal(Integer id, Object factor, Class<T> targetClass) {
        JSONObject jsonArray = JSONObject.parseObject("{}");
        JSONObject targetJson = jsonArray.getJSONObject(String.valueOf(id));
        int startIndex = targetJson.getInteger(KEY_ID);
        int length = targetJson.getInteger(KEY_LENGTH);

        if (factor instanceof Float) {
            float result = getByteVal(this.data8, startIndex, length).getInt() * ((Float) factor);
            return targetClass.cast(result);
        } else if (factor instanceof Integer) {
            Integer result = getByteVal(this.data8, startIndex, length).getInt() * ((Integer) factor);
            return targetClass.cast(result);
        }

        return null;
    }

    public static final String KEY_PHASE_A_VOL = "phaseAVoltage";
    public static final String KEY_PHASE_B_VOL = "phaseBVoltage";
    public static final String KEY_PHASE_C_VOL = "phaseCVoltage";
    public static final String KEY_FREQ = "freq";
    public static final String KEY_PHASE_A_CURT = "phaseACurt";
    public static final String KEY_PHASE_B_CURT = "phaseBCurt";
    public static final String KEY_PHASE_C_CURT = "phaseCCurt";
    public static final String KEY_BATTERY_PERCT = "batteryPercent";
    public static final String KEY_CITY_ELEC = "cityElec";
    public static final String KEY_LOADED_FLAG = "loaded";
    public static final String KEY_TOTAL_ELEC = "loaded";
    private static final String KEY_TOTAL_RUN_TIME = "";

    public void getPhaseAVoltage(int id, byte[] byteData, Map<String, Object> data) {
        switch (id) {
            case DYNAMIC_MSG_VOL1_ID:
                float volFactor = 0.01f;
                float freqFactor = 0.1f;
                data.put(KEY_PHASE_A_VOL, getByteVal(byteData, 0, 2).getInt() * volFactor);
                data.put(KEY_PHASE_B_VOL, getByteVal(byteData, 2, 2).getInt() * volFactor);
                data.put(KEY_PHASE_C_VOL, getByteVal(byteData, 4, 2).getInt() * volFactor);
                data.put(KEY_FREQ, getByteVal(byteData, 6, 2).getInt() * freqFactor);
                break;
            case DYNAMIC_MSG_VOL2_ID:
                float curtFactor = 0.001f;
                data.put(KEY_PHASE_A_CURT, getByteVal(byteData, 0, 2).getInt() * curtFactor);
                data.put(KEY_PHASE_B_CURT, getByteVal(byteData, 2, 2).getInt() * curtFactor);
                data.put(KEY_PHASE_C_CURT, getByteVal(byteData, 4, 2).getInt() * curtFactor);
                data.put(KEY_BATTERY_PERCT, getByteVal(byteData, 6, 1).getInt());
                data.put(KEY_CITY_ELEC, getBitVal(byteData, 7, 2));
                data.put(KEY_LOADED_FLAG, getBitVal(byteData, 7, 3));
                break;
            case DYNAMIC_MSG_VOL3_ID:
                float totalElecFactor = 0.01f;
                data.put(KEY_TOTAL_ELEC, getByteVal(byteData, 4, 4).getInt() * totalElecFactor);
                break;
            case DYNAMIC_MSG_VOL4_ID:
                data.put(KEY_TOTAL_RUN_TIME, getByteVal(byteData, 4, 4).getInt());
                break;
            default:
                break;
        }
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
