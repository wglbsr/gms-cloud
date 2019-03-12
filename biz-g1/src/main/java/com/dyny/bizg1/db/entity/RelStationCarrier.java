package com.dyny.bizg1.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@TableName("gms_rel_station_carrier")
public class RelStationCarrier extends Model<RelStationCarrier> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "station_id", type = IdType.AUTO)
    private Integer stationId;

    private Integer carrierId;


    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    @Override
    protected Serializable pkVal() {
        return this.stationId;
    }

    @Override
    public String toString() {
        return "RelStationCarrier{" +
        "stationId=" + stationId +
        ", carrierId=" + carrierId +
        "}";
    }
}
