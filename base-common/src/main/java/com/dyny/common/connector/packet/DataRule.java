package com.dyny.common.connector.packet;

import com.dyny.common.utils.Utils;
import lombok.Data;
import org.apache.tomcat.util.buf.HexUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wglbs
 * @Date: 2019/8/15 20:09
 * @Description:
 * @Version 1.0.0
 */
@Data
public class DataRule {
    private Integer communicateId;
    private String key;
    private Integer startIndex;
    private Integer size;
    private Integer bitIndex = null;
    private Object factor;
    private Class targetClass;
    private Class oriClass;
    private static Integer LENGTH_ID = 4;
    private static Integer LENGTH_PER_DATA = 12;
    private String prefix;
    private String suffix;

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

    public static Map<String, Object> getVal(byte[] payloadBytes0) {
        Map<Integer, List<DataRule>> dataRulesMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i * LENGTH_PER_DATA < payloadBytes0.length; i++) {
            int index = i * LENGTH_PER_DATA;
            byte[] key = ByteUtils.subArray(payloadBytes0, index, index + LENGTH_ID);
            List<DataRule> dataRuleList = dataRulesMap.get(Utils.Byte.bytes2int(key));
            for (DataRule dataRule : dataRuleList) {
                int startIndex = index + LENGTH_ID + dataRule.getStartIndex();
                int end = startIndex + dataRule.getSize();
                String dataKey = dataRule.getKey();
                //非布尔型
                if (dataRule.getBitIndex() == null) {
                    //浮点型
                    byte[] value = ByteUtils.subArray(payloadBytes0, startIndex, end);
                    //非字符型
                    if (dataRule.getTargetClass() != String.class) {
                        Object factor = dataRule.getFactor();
                        if (factor == null) {
//                            data.put(dataKey, );

                        } else {

                            data.put(dataKey, ByteBuffer.wrap(value).getFloat() * (float) factor);

                        }
                        //整型
//                    } else if (dataRule.getTargetClass() == Integer.class) {
//                        Object factor = dataRule.getFactor();
//                        if (factor == null) {
//                            data.put(dataKey, ByteBuffer.wrap(value).getInt());
//                        } else {
//                            data.put(dataKey, ByteBuffer.wrap(value).getInt() * (Integer) (factor));
//                        }
//                        data.put(dataKey, Utils.Byte.bytes2int(value));
                        //字符型
                    } else if (dataRule.getTargetClass() == String.class) {
                        String result = HexUtils.toHexString(value);
                        String suffix = dataRule.getSuffix();
                        String prefix = dataRule.getPrefix();
                        //是否添加前缀后缀
                        if (suffix != null) {
                            result = result + suffix;
                        }
                        if (prefix != null) {
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


        return null;
    }

    private <T> T getFromBytes(byte[] valueByte, Class<T> oriClass, Object factor) {
        if (oriClass == Float.class) {
            ByteBuffer.wrap(valueByte).getFloat();
        }
        return null;
    }

    private DataRule getFromList(List<DataRule> dataRuleList, byte[] id4) {
        Integer id = Utils.Byte.bytes2int(id4);

        return null;
    }

    public static void main(String[] args) {


    }

}
