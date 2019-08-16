package com.dyny.common.connector.packet;

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
    private Object factor;
    private Class targetClass;
    private Class oriClass;
    private String prefix;
    private String suffix;

    public DataRule(Integer communicateId, String key, Integer startIndex, Integer size, Integer bitIndex, Object factor, Class targetClass, Class oriClass, String prefix, String suffix) {
        this.communicateId = communicateId;
        this.key = key;
        this.startIndex = startIndex;
        this.size = size;
        this.bitIndex = bitIndex;
        this.factor = factor;
        this.targetClass = targetClass;
        this.oriClass = oriClass;
        this.prefix = prefix;
        this.suffix = suffix;
    }


    public static void main(String[] args) {


    }

}
