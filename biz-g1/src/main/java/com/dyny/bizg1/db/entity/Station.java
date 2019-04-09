package com.dyny.bizg1.db.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@TableName("gms_station")
public class Station extends Model<Station> {

    private static final long serialVersionUID = 1L;
//    @Excel(name = "经度", width = 30, isImportField = "true_st")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @Excel(name = "站址编码", width = 30, isImportField = "true_st")
    private String code;

    @Excel(name = "纬度", width = 30, isImportField = "true_st")
    private BigDecimal latitude;

    @Excel(name = "经度", width = 30, isImportField = "true_st")
    private BigDecimal longitude;

    @Excel(name = "站址名称", width = 30, isImportField = "true_st")
    private String name;

    @Excel(name = "省份", width = 30, isImportField = "true_st")
    private String province;

    @Excel(name = "地市", width = 30, isImportField = "true_st")
    private String city;

    @Excel(name = "区县", width = 30, isImportField = "true_st")
    private String district;

    @Excel(name = "备注", width = 30, isImportField = "true_st")
    private String description;

    private Integer regionId;

//    @Excel(name = "地区id", width = 30, isImportField = "true_st")
    private Integer customerId;
    @Excel(name = "详细地址", width = 30, isImportField = "true_st")
    private String address;
    @Excel(name = "站址备注", width = 30, isImportField = "true_st")
    private String addressRemark;
    @Excel(name = "产权属性", width = 30, isImportField = "true_st" ,replace={"自建_0","注入_1"})
    private Integer type;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", code=" + code +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", name=" + name +
                ", description=" + description +
                ", regionId=" + regionId +
                ", address=" + address +
                ", type=" + type +
                "}";
    }
}
