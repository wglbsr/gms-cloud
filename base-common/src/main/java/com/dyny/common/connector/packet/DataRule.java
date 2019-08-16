package com.dyny.common.connector.packet;

import com.dyny.common.utils.GDPayloadUtils;
import lombok.Data;

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
    private Integer bitIndex;
    private String factor;
    private Integer factorClass;
    private Integer targetClass;
    private Integer oriClass;
    private String prefix;
    private String suffix;

    /**
     * @return
     * @Author wanggl(lane)
     * @Description //TODO 通用规则
     * @Date 14:18 2019/8/16
     * @Param [communicateId, key, startIndex, size, bitIndex, factor, factorClass, targetClass, oriClass, prefix, suffix]
     **/
    public DataRule(Integer communicateId, String key, Integer startIndex, Integer size, Integer bitIndex, String factor, int factorClass, int targetClass, int oriClass, String prefix, String suffix) {
        this.communicateId = communicateId;
        this.key = key;
        this.startIndex = startIndex;
        this.size = size;
        this.bitIndex = bitIndex;
        this.factor = factor;
        this.targetClass = targetClass;
        this.factorClass = factorClass;
        this.oriClass = oriClass;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * @Author wanggl(lane)
     * @Description //TODO 纯字符型
     * @Date 14:56 2019/8/16
     * @Param [communicateId, key, startIndex, size, bitIndex, factor, factorClass, targetClass, oriClass, prefix, suffix]
     * @return
     **/
    public DataRule(Integer communicateId, String key, Integer startIndex, Integer size, String prefix, String suffix) {
        this.communicateId = communicateId;
        this.key = key;
        this.targetClass = 3;
        this.startIndex = startIndex;
        this.size = size;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * @return
     * @Author wanggl(lane)
     * @Description //TODO 非字符规则
     * @Date 14:18 2019/8/16
     * @Param [communicateId, key, startIndex, size, bitIndex, factor, factorClass, targetClass, oriClass]
     **/
    public DataRule(Integer communicateId, String key, Integer startIndex, Integer size, Integer bitIndex, String factor, int factorClass, int targetClass, int oriClass) {
        this.communicateId = communicateId;
        this.key = key;
        this.startIndex = startIndex;
        this.size = size;
        this.bitIndex = bitIndex;
        this.factor = factor;
        this.targetClass = targetClass;
        this.factorClass = factorClass;
        this.oriClass = oriClass;
    }

    /**
     * @return
     * @Author wanggl(lane)
     * @Description //TODO 非布尔非字符规则,即数值型
     * @Date 14:21 2019/8/16
     * @Param [communicateId, key, startIndex, size, factor, factorClass, targetClass, oriClass]
     **/
    public DataRule(Integer communicateId, String key, Integer startIndex, Integer size, String factor, int factorClass, int targetClass, int oriClass) {
        this.communicateId = communicateId;
        this.key = key;
        this.startIndex = startIndex;
        this.size = size;
        this.factor = factor;
        this.targetClass = targetClass;
        this.factorClass = factorClass;
        this.oriClass = oriClass;
    }

    /**
     * @return
     * @Author wanggl(lane)
     * @Description //TODO 布尔规则
     * @Date 14:16 2019/8/16
     * @Param [communicateId, key, startIndex, bitIndex]
     **/
    public DataRule(Integer communicateId, String key, Integer startIndex, Integer bitIndex) {
        this.communicateId = communicateId;
        this.key = key;
        this.size = 1;
        this.startIndex = startIndex;
        this.bitIndex = bitIndex;
    }


    public static void main(String[] args) {


    }

}
