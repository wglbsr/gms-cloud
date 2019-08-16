package com.dyny.common.utils;

import com.dyny.common.connector.packet.DataRule;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wglbs
 * @Date: 2019/8/14 10:17
 * @Description:
 * @Version 1.0.0
 */
public class GDPayloadUtils {
    /**
     * @return byte[]
     * @Author wanggl(lane)
     * @Description //TODO
     * @Date 10:47 2019/8/14
     * @Param [id4, data8]
     **/
    public static byte[] combinePayload(byte[] id4, byte[] data8) {
        return ByteUtils.concatenate(id4, data8);
    }

    public static byte[] combinePayload(Integer id4, byte... data8) {
        return combinePayload(Utils.Byte.int2bytes(id4), data8);
    }

    public static byte[] combinePayload(Integer id4, int size, byte... data) {
        byte[] tempData;
        if (size == 0 || size == data.length) {
            tempData = data;
        } else {
            tempData = new byte[size];
            for (int i = 0; i < size; i++) {
                if (i >= data.length) {
                    tempData[i] = 0x00;
                } else {
                    tempData[i] = data[i];
                }
            }
        }
        return combinePayload(Utils.Byte.int2bytes(id4), tempData);
    }


    /**
     * @return java.util.Map<java.lang.Integer, byte [ ]>
     * @Author wanggl(lane)
     * @Description //TODO 将会更改参数的值
     * @Date 10:47 2019/8/14
     * @Param [payloadBytes0]
     **/


    private static ByteBuffer getByteVal(byte[] data, int start, int size) {
        byte[] temp = ArrayUtils.subarray(data, start, start + size);
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

    private static final Integer LENGTH_ID = 4;
    private static final Integer LENGTH_PER_DATA = 12;
    private static Logger logger = LoggerFactory.getLogger(GDPayloadUtils.class);

    public static Map<String, Object> getVal(byte[] payloadBytes0, Map<Integer, List<DataRule>> dataRulesMap) {
//        Map<Integer, List<DataRule>> dataRulesMap = new HashMap<>();
        if (dataRulesMap == null || dataRulesMap.isEmpty()) {
            logger.info("没有规则来获取数据!");
            return null;
        }
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; (i + 1) * LENGTH_PER_DATA <= payloadBytes0.length; i++) {
            //每个数据段的起始下标
            int index = i * LENGTH_PER_DATA;
            //通讯id,长度为LENGTH_ID
            byte[] key = ArrayUtils.subarray(payloadBytes0, index, index + LENGTH_ID);
            List<DataRule> dataRuleList = dataRulesMap.get(Utils.Byte.bytes2int(key));
            if (dataRuleList == null || dataRuleList.isEmpty()) {
                logger.info("数据规则长度为零!");
                return null;
            }
            for (DataRule dataRule : dataRuleList) {
                Class targetClass = getClass(dataRule.getTargetClass());
                Class oriClass = getClass(dataRule.getOriClass());
                int startIndex = index + LENGTH_ID + dataRule.getStartIndex();
                int size = dataRule.getSize();
                String dataKey = dataRule.getKey();
                //非布尔型
                if (dataRule.getBitIndex() == null) {
                    //浮点型
                    byte[] value = ArrayUtils.subarray(payloadBytes0, startIndex, startIndex + size);
                    //非字符型
                    if (targetClass != String.class) {
                        Object factor = dataRule.getFactor();
                        data.put(dataKey, getFromBytes(value, oriClass, targetClass, factor));
                    } else if (targetClass == String.class) {
                        String result = HexUtils.toHexString(value);
                        String suffix = dataRule.getSuffix();
                        String prefix = dataRule.getPrefix();
                        //是否添加前缀后缀
                        if (StringUtils.isNotEmpty(suffix)) {
                            result = result + suffix;
                        }
                        if (StringUtils.isNotEmpty(prefix)) {
                            result = prefix + result;
                        }
                        data.put(dataKey, result);
                    }
                    //布尔型
                } else {
                    Boolean value = getBitVal(payloadBytes0, startIndex, dataRule.getBitIndex());
                    data.put(dataRule.getKey(), value);
                }
            }
        }
        return data;
    }

    private static Class getClass(int classId) {
        switch (classId) {
            case 0:
                return Boolean.class;
            case 1:
                return Integer.class;
            case 2:
                return Float.class;
            case 3:
                return String.class;
            default:
                return Boolean.class;
        }
    }

    private static <T> T getFromBytes(byte[] valueByte, Class<T> oriClass, Class<T> targetClass, Object factor) {
        if (oriClass == Float.class) {
            float floatVal = ByteBuffer.wrap(valueByte).getFloat();
            if (factor instanceof Float) {
                return targetClass.cast(floatVal * (Float) factor);
            } else if (factor instanceof Integer) {
                return targetClass.cast(floatVal * (Integer) factor);
            } else if (factor == null) {
                return targetClass.cast(floatVal);
            }
        } else if (oriClass == Integer.class) {
            int intVal = (new BigInteger(valueByte)).intValue();
            if (factor instanceof Float) {
                return targetClass.cast(intVal * (Float) factor);
            } else if (factor instanceof Integer) {
                return targetClass.cast(intVal * (Integer) factor);
            } else if (factor == null) {
                return targetClass.cast(intVal);
            }
        }
        return null;
    }

    public static final int DYNAMIC_MSG_VOL1_ID = 0x1C006501;
    public static final int DYNAMIC_MSG_CURT_ID = 0x1C006502;
    public static final int DYNAMIC_MSG_VOL3_ID = 0x1C006504;
    public static final int DYNAMIC_MSG_VOL4_ID = 0x1C006505;
    public static final int DYNAMIC_MSG_VOL5_ID = 0x1C00650B;
    public static final int DYNAMIC_MSG_VOL6_ID = 0x1C00651B;
    public static final int DYNAMIC_MSG_VOL7_ID = 0x1C00651A;
    public static final int DYNAMIC_MSG_VOL8_ID = 0x1C00651C;
    public static final int DYNAMIC_MSG_VOL9_ID = 0x1C00651D;
    public static final int DYNAMIC_MSG_VOL10_ID = 0x1C00651E;

    public static final int STATISTIC_MSG_VOL1_ID = 0x1C006506;
    public static final int STATISTIC_MSG_VOL2_ID = 0x1C006507;
    public static final int STATISTIC_MSG_VOL3_ID = 0x1C006508;
    public static final int STATISTIC_MSG_VOL4_ID = 0x1C006509;
    public static final int STATISTIC_MSG_VOL5_ID = 0x1C00650A;
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

    public static void main(String[] args) {

    }

}
