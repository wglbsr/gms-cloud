package com.dyny.gdmodule.utils;

import com.dyny.common.utils.Utils;
import com.dyny.gdmodule.db.entity.DataRule;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class PayloadUtils {

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
    private static Logger logger = LoggerFactory.getLogger(PayloadUtils.class);

    public static Map<String, Object> getVal(byte[] payloadBytes0, Map<Integer, List<DataRule>> dataRulesMap) {
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
                continue;
            }
            for (DataRule dataRule : dataRuleList) {
                int startIndex = index + LENGTH_ID + dataRule.getStartIndex();
                int size = dataRule.getSize();
                String dataKey = dataRule.getDataKey();
                logger.info("{}", dataKey);
                //非布尔型
                if (dataRule.getBitIndex() == null) {
                    byte[] value = ArrayUtils.subarray(payloadBytes0, startIndex, startIndex + size);
                    //非字符型
                    Class targetClass = getClass(dataRule.getTargetClass());
                    int calcType = dataRule.getFactorCalcType();
                    if (targetClass != String.class) {
                        String factor = dataRule.getFactor();
                        Class oriClass = getClass(dataRule.getOriClass());
                        Class factorClass = getClass(dataRule.getFactorClass());
                        data.put(dataKey, getFromBytes(value, oriClass, targetClass, factorClass, factor, calcType));
                    } else {
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
                    data.put(dataRule.getDataKey(), value);
                }
            }
        }
        return data;
    }

    private static Class getClass(Integer classId) {
        if (classId == null) {
            return null;
        }
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
                return Integer.class;
        }
    }


    /**
     * @return T
     * @Author wanggl(lane)
     * @Description //TODO 根据设定类型获取值,冗余代码较多,后续再优化........使用三目运算符户出现两个整型相加变成浮点的问题,不知是否是写法的问题
     *
     * @Date 15:56 2019-09-02
     * @Param [valueByte, oriClass, targetClass, factorClass, factor, calcType]
     **/
    private static <T> T getFromBytes(byte[] valueByte, Class<T> oriClass, Class<T> targetClass, Class<T> factorClass, String factor, int calcType) {
        if (factor != null && factorClass != null) {
            Object result;
            switch (calcType) {
                case 1:
                    if (oriClass == Float.class) {
                        if (factorClass == Float.class) {
                            result = ByteBuffer.wrap(valueByte).getFloat() + Float.parseFloat(factor);
                        } else {
                            result = ByteBuffer.wrap(valueByte).getFloat() + Integer.parseInt(factor);
                        }
                    } else {
                        if (factorClass == Float.class) {
                            result = Utils.Byte.bytes2int(valueByte) + Float.parseFloat(factor);
                        } else {
                            result = Utils.Byte.bytes2int(valueByte) + Integer.parseInt(factor);
                        }
                    }
                    break;
                case 2:
                    if (oriClass == Float.class) {
                        if (factorClass == Float.class) {
                            result = ByteBuffer.wrap(valueByte).getFloat() - Float.parseFloat(factor);
                        } else {
                            result = ByteBuffer.wrap(valueByte).getFloat() - Integer.parseInt(factor);
                        }
                    } else {
                        if (factorClass == Float.class) {
                            result = Utils.Byte.bytes2int(valueByte) - Float.parseFloat(factor);
                        } else {
                            result = Utils.Byte.bytes2int(valueByte) - Integer.parseInt(factor);
                        }
                    }
                    break;
                case 3:
                    if (oriClass == Float.class) {
                        if (factorClass == Float.class) {
                            result = ByteBuffer.wrap(valueByte).getFloat() * Float.parseFloat(factor);
                        } else {
                            result = ByteBuffer.wrap(valueByte).getFloat() * Integer.parseInt(factor);
                        }
                    } else {
                        if (factorClass == Float.class) {
                            result = Utils.Byte.bytes2int(valueByte) * Float.parseFloat(factor);
                        } else {
                            result = Utils.Byte.bytes2int(valueByte) * Integer.parseInt(factor);
                        }
                    }
                    break;
                case 4:
                    if (oriClass == Float.class) {
                        if (factorClass == Float.class) {
                            result = ByteBuffer.wrap(valueByte).getFloat() / Float.parseFloat(factor);
                        } else {
                            result = ByteBuffer.wrap(valueByte).getFloat() / Integer.parseInt(factor);
                        }
                    } else {
                        if (factorClass == Float.class) {
                            result = Utils.Byte.bytes2int(valueByte) / Float.parseFloat(factor);
                        } else {
                            result = Utils.Byte.bytes2int(valueByte) / Integer.parseInt(factor);
                        }
                    }
                    break;
                default:
                    if (oriClass == Float.class) {
                        if (factorClass == Float.class) {
                            result = ByteBuffer.wrap(valueByte).getFloat() * Float.parseFloat(factor);
                        } else {
                            result = ByteBuffer.wrap(valueByte).getFloat() * Integer.parseInt(factor);
                        }
                    } else {
                        if (factorClass == Float.class) {
                            result = Utils.Byte.bytes2int(valueByte) * Float.parseFloat(factor);
                        } else {
                            result = Utils.Byte.bytes2int(valueByte) * Integer.parseInt(factor);
                        }
                    }
                    break;
            }
            return targetClass.cast(result);
        } else {
            if (oriClass == Float.class) {
                return targetClass.cast(ByteBuffer.wrap(valueByte).getFloat());
            } else {
                return targetClass.cast(Utils.Byte.bytes2int(valueByte));
            }
        }
    }
}
