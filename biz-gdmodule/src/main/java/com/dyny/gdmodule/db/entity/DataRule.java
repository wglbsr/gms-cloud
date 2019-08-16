package com.dyny.gdmodule.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wanggl
 * @since 2019-08-16
 */
@Data
@TableName("data_rule")
public class DataRule extends Model<DataRule> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "data_key", type = IdType.INPUT)
    private String dataKey;

    private Integer communicateId;

    private Integer startIndex;

    private Integer size;

    private Integer bitIndex;

    private String factor;

    private Integer factorClass;

    private Integer targetClass;

    private Integer oriClass;

    private String prefix;

    private String suffix;

//    /**
//     * @return
//     * @Author wanggl(lane)
//     * @Description //TODO 通用规则
//     * @Date 14:18 2019/8/16
//     * @Param [communicateId, key, startIndex, size, bitIndex, factor, factorClass, targetClass, oriClass, prefix, suffix]
//     **/
//    public DataRule(Integer communicateId, String dataKey, Integer startIndex, Integer size, Integer bitIndex, String factor, int factorClass, int targetClass, int oriClass, String prefix, String suffix) {
//        this.communicateId = communicateId;
//        this.dataKey = dataKey;
//        this.startIndex = startIndex;
//        this.size = size;
//        this.bitIndex = bitIndex;
//        this.factor = factor;
//        this.targetClass = targetClass;
//        this.factorClass = factorClass;
//        this.oriClass = oriClass;
//        this.prefix = prefix;
//        this.suffix = suffix;
//    }
//
//    /**
//     * @Author wanggl(lane)
//     * @Description //TODO 纯字符型
//     * @Date 14:56 2019/8/16
//     * @Param [communicateId, key, startIndex, size, bitIndex, factor, factorClass, targetClass, oriClass, prefix, suffix]
//     * @return
//     **/
//    public DataRule(Integer communicateId, String dataKey, Integer startIndex, Integer size, String prefix, String suffix) {
//        this.communicateId = communicateId;
//        this.dataKey = dataKey;
//        this.targetClass = 3;
//        this.startIndex = startIndex;
//        this.size = size;
//        this.prefix = prefix;
//        this.suffix = suffix;
//    }
//
//    /**
//     * @return
//     * @Author wanggl(lane)
//     * @Description //TODO 非字符规则
//     * @Date 14:18 2019/8/16
//     * @Param [communicateId, key, startIndex, size, bitIndex, factor, factorClass, targetClass, oriClass]
//     **/
//    public DataRule(Integer communicateId, String dataKey, Integer startIndex, Integer size, Integer bitIndex, String factor, int factorClass, int targetClass, int oriClass) {
//        this.communicateId = communicateId;
//        this.dataKey = dataKey;
//        this.startIndex = startIndex;
//        this.size = size;
//        this.bitIndex = bitIndex;
//        this.factor = factor;
//        this.targetClass = targetClass;
//        this.factorClass = factorClass;
//        this.oriClass = oriClass;
//    }
//
//    /**
//     * @return
//     * @Author wanggl(lane)
//     * @Description //TODO 非布尔非字符规则,即数值型
//     * @Date 14:21 2019/8/16
//     * @Param [communicateId, key, startIndex, size, factor, factorClass, targetClass, oriClass]
//     **/
//    public DataRule(Integer communicateId, String dataKey, Integer startIndex, Integer size, String factor, int factorClass, int targetClass, int oriClass) {
//        this.communicateId = communicateId;
//        this.dataKey = dataKey;
//        this.startIndex = startIndex;
//        this.size = size;
//        this.factor = factor;
//        this.targetClass = targetClass;
//        this.factorClass = factorClass;
//        this.oriClass = oriClass;
//    }
//
//    /**
//     * @return
//     * @Author wanggl(lane)
//     * @Description //TODO 布尔规则
//     * @Date 14:16 2019/8/16
//     * @Param [communicateId, key, startIndex, bitIndex]
//     **/
//    public DataRule(Integer communicateId, String dataKey, Integer startIndex, Integer bitIndex) {
//        this.communicateId = communicateId;
//        this.dataKey = dataKey;
//        this.size = 1;
//        this.startIndex = startIndex;
//        this.bitIndex = bitIndex;
//    }

    @Override
    protected Serializable pkVal() {
        return this.dataKey;
    }
}
