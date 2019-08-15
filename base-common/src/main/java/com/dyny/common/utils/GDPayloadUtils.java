package com.dyny.common.utils;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;
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
    public static Map<Integer, byte[]> decodePayload(byte[] payloadBytes0) {
        if (payloadBytes0 == null || payloadBytes0.length == 0 || payloadBytes0.length % 12 != 0) {
            return null;
        }
        Map<Integer, byte[]> payloadMap = new HashMap<>();
        for (int i = 0; i * 12 < payloadBytes0.length; i++) {
            int index = i * 12;
            int id = ByteBuffer.wrap(payloadBytes0, index, 4).getInt();
            byte[] dataUnit = ByteUtils.subArray(payloadBytes0, index + 4, 12);
            payloadMap.put(id, dataUnit);
        }
        return payloadMap;
    }
}
