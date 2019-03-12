package com.dyny.bizg1.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@TableName("gms_generator")
public class Generator extends Model<Generator> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer customerId;

    private Integer stationId;

    private String code;

    /**
     * 0.未启用 1.调试中 2.调试完成  3.安装中  4.安装完成
     */
    private Integer status;

    private Integer power;

    /**
     * 燃油类型 1.柴油 0.汽油
     */
    private Integer fuleType;

    /**
     * 电流类型 1.交流  0.直流
     */
    private Integer currentType;

    /**
     * 是否可远程控制
     */
    private Boolean remotable;

    /**
     * 油箱容量
     */
    private Integer tankCapacity;

    private LocalDateTime createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public Integer getFuleType() {
        return fuleType;
    }

    public void setFuleType(Integer fuleType) {
        this.fuleType = fuleType;
    }

    public Integer getCurrentType() {
        return currentType;
    }

    public void setCurrentType(Integer currentType) {
        this.currentType = currentType;
    }

    public Boolean getRemotable() {
        return remotable;
    }

    public void setRemotable(Boolean remotable) {
        this.remotable = remotable;
    }

    public Integer getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(Integer tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Generator{" +
        "id=" + id +
        ", customerId=" + customerId +
        ", stationId=" + stationId +
        ", code=" + code +
        ", status=" + status +
        ", power=" + power +
        ", fuleType=" + fuleType +
        ", currentType=" + currentType +
        ", remotable=" + remotable +
        ", tankCapacity=" + tankCapacity +
        ", createTime=" + createTime +
        "}";
    }
}
