package com.dyny.gdmodule.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wanggl
 * @since 2019-09-02
 */
@Data
@TableName("data_rule")
public class DataRule extends Model<DataRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 数据保存用的key,唯一
     */
    @TableId(value = "data_key", type = IdType.AUTO)
    private String dataKey;

    /**
     * 通讯id
     */
    private Integer communicateId;

    /**
     * 字节开始下标,即从第几个字节开始获取,包括
     */
    private Integer startIndex;

    /**
     * 获取的字节长度
     */
    private Integer size;

    /**
     * 位的下标，即获取布尔值的位的下标,包括
     */
    private Integer bitIndex;

    /**
     * 因数，获取后的数值将会与因数相乘
     */
    private String factor;

    /**
     * 因数的类型，0布尔型；1整型；2浮点型；3字符型。下同
     */
    private Integer factorClass;

    /**
     * 返回结果的类型
     */
    private Integer targetClass;

    /**
     * 从字节解析的类型
     */
    private Integer oriClass;

    /**
     * 前缀，内容为字符时有效
     */
    private String prefix;

    /**
     * 后缀，内容为字符时有效
     */
    private String suffix;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 状态
     */
    private Integer status;

    private Integer type;

    private Integer factorCalcType;

    private String remark;



}
