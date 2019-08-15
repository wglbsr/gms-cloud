package com.dyny.gdmodule.db.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wanggl
 * @since 2019-08-13
 */
@Data
@TableName("packet")
public class Packet extends Model<Packet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 必填
     */
    private String callerUsername;

    /**
     * 必填
     */
    private String callerPassword;

    /**
     * 必填
     */
    private Integer producer;

    /**
     * 必填
     */
    private String spName;

    /**
     * 必填
0、停止发电，1、开始发电，2未发电，3、发电中
     */
    private Integer state;

    /**
     * 必填
     */
    private String serialNum;

    /**
     * 状态0、1有值，其它为空
     */
    private LocalDateTime beginTime;

    /**
     * 状态0、1有值，其它为空
     */
    private LocalDateTime endTime;

    /**
     * 必填
     */
    private LocalDateTime updateTime;

    /**
     * 必填
     */
    private String sid;

    /**
     * 必填
     */
    private String bid;

    /**
     * 必填
     */
    private BigDecimal spLng;

    /**
     * 必填
     */
    private BigDecimal spLat;

    /**
     * 非必填，
     */
    private BigDecimal lat;

    /**
     * 非必填，
     */
    private BigDecimal lng;

    /**
     * 非必填，
     */
    private Integer distance;

    /**
     * 必填
     */
    private BigDecimal oilCost;

    /**
     * 非必填，网络类型，例如：4G网络
     */
    private String mnc;

    /**
     * 非必填，
     */
    private BigDecimal totalOilNum;

    /**
     * 非必填，
     */
    private BigDecimal avgCurrent;

    /**
     * 非必填，
     */
    private BigDecimal avgVoltage;

    /**
     * 非必填，
     */
    private Integer avgElecFrequency;

    /**
     * 状态0有值，其它为空
     */
    private Integer totalGenerateNum;

    /**
     * 状态3有值，其它为空
     */
    private Integer volA;

    /**
     * 状态3有值，其它为空
     */
    private Integer volB;

    /**
     * 状态3有值，其它为空
     */
    private Integer volC;

    /**
     * 状态3有值，其它为空
     */
    private Integer curtA;

    /**
     * 状态3有值，其它为空
     */
    private Integer curtB;

    /**
     * 状态3有值，其它为空
     */
    private Integer curtC;

    /**
     * 信号强度?状态2、3有值，其它为空
     */
    private Integer battery;

    /**
     * 状态0、3有值，其它为空
     */
    private BigDecimal elecFrequency;

    /**
     * 有功功率,状态3有值，其它为空
     */
    private Integer usePower;

    /**
     * 无功功率,状态3有值，其它为空
     */
    private Integer nousePower;

    /**
     * 视在功率,状态3有值，其它为空
     */
    private Integer apparPower;

    /**
     * 功率因数,状态3有值，其它为空
     */
    private BigDecimal powerFactor;

    /**
     * 状态3有值，其它为空
        故障上报以及故障恢复的流程，1-8都有，取值1-4 代表故障恢复，取值5-8代表故障上报；
        1、油机发电，     2、油机发电且空载，
        3、市电发电，      4、市电且空载，
        5、故障且油机发电，6、故障、油机发电且空载，
        7、故障且市电发电，8、故障、市电且空载

     */
    private Integer elecType;

    /**
     * 必填
     */
    private Integer oilPercent;

    /**
     * 必填
     */
    private BigDecimal batteryVol;

    /**
     * 非必填，本次发电时长
     */
    private BigDecimal elecLast;

    /**
     * 非必填,
     */
    private BigDecimal powerNumA;

    /**
     * 非必填,
     */
    private BigDecimal powerNumB;

    /**
     * 非必填,
     */
    private BigDecimal powerNumC;

    /**
     * 非必填,
     */
    private Integer powerTotalNum;

    /**
     * 非必填,三相实际总电能
     */
    private Integer generateNum;

    /**
     * 非必填
     */
    private BigDecimal oilNum;



    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
