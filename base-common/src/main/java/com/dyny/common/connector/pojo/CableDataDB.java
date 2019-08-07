package com.dyny.common.connector.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: wglbs
 * @Date: 2019/8/6 15:18
 * @Description:
 * @Version 1.0.0
 */
@Data
public class CableDataDB {
    //	状态	0、停止发电，1、开始发电，2未发电，3、发电中	必填
    private Integer state = null;

    private Date beginTime = null;//	开始发电时间	yyyy-mm-dd hh:mm:ss	状态0、1有值，其它为空
    private Date endtime = null;//	停止发电时间	yyyy-mm-dd hh:mm:ss	状态0有值，其它为空


    private String callerUser = null;//	调用方用户名	例如：kexin_123	必填
    private String callerPwd = null;//	调用方密码	例如：123456	必填
    private String producer = null;//	厂家名称	例如：成都科鑫（不超过10个字）	必填
    private String spName = null;//	分机号	分机号	必填
    private String serialNum = null;//	流水号	发电数据唯一关联标识	必填


    private Date updateTime = null;//	   更新时间	yyyy-mm-dd hh:mm:ss	必填
    private String sid = null;//	LAC/SID	局站BID 号,lac	必填
    private String bid = null;//	CellID/BID	局站BID 号,cell
    private BigDecimal lng = null;//	经度	基站经度（106.80407）保留6位小数	非必填
    private BigDecimal lat = null;//	纬度	基站纬度（30.65591）保留6位小数
    private BigDecimal spLng = null;//	发电机经度	发电机经度（106.80407）保留6位小数	必填
    private BigDecimal spLat = null;//	发电机纬度	发电机纬度（30.65591）保留6位小数	必填
    private Integer distance = null;//	距离	发电机与基站距离	非必填
    private Float oilCost = null;//	油耗	油耗（L/H）	必填
    private Integer mnc = null;//	网络类型	例如：4G网络	非必填
    private Float totalOilNum = null;//	总用油量	小数（L）	非必填
    private Float avgCurrent = null;//	平均电流	小数（A）	非必填
    private Integer avgVoltage = null;//	平均电压	（V）	非必填
    private Integer avgElecFrequency = null;//	发电频率	（HZ）	非必填

    private Integer totalGenerateNum = null;//	总实发电量	实发电量（度）	状态0有值，其它为空


    /*******************************************发电瞬态信息***************************************************************/
    //	发电类别	,状态3有值，其它为空,故障上报以及故障恢复的流程，1-8都有，取值1-4 代表故障恢复，取值5-8代表故障上报；
    //  	    1、油机发电，      2、油机发电且空载，
    //		    3、市电发电，      4、市电且空载，
    //		    5、故障且油机发电，6、故障、油机发电且空载，
    //		    7、故障且市电发电，8、故障、市电且空载
    private Integer elecType = null;

    private Integer volA = null;//	A相电压	单位 （ V ）整数	状态3有值，其它为空
    private Integer volB = null;//	B相电压	单位 （ V ）整数	状态3有值，其它为空
    private Integer volC = null;//	C相电压	单位 （ V ）整数	状态3有值，其它为空
    private Float curtA = null;//	A相电流	单位 （ A ）保留2位小数	状态3有值，其它为空
    private Float curtB = null;//	B相电流	单位 （ A ）保留2位小数	状态3有值，其它为空
    private Float curtC = null;//	C相电流	单位 （ A ）保留2位小数	状态3有值，其它为空
    private Integer nousePower = null;//	无功功率	单位 （Kw ）	状态3有值，其它为空
    private Integer apparPower = null;//	视在功率	单位 （Kw ）	状态3有值，其它为空
    private Integer usePower = null;//	有功功率	单位 （Kw ）	状态3有值，其它为空
    private Integer powerFactor = null;//	功率因数		状态3有值，其它为空

    private Integer battery = null;//	信号强度	单位 （ % ）模块位置所接收信号强度	状态2、3有值，其它为空
    private Float elecFrequency = null;//	发电频率	单位 （Hz ）保留2位小数	状态0、3有值，其它为空

    private Integer oilPercent = null;//	剩余油量	 单位（%）整数	必填
    private Float batteryVol = null;//	蓄电池电压值	 单位（V）保留2位小数	必填
    private Integer elecLast = null;//	本次发电时长	时长（H）	非必填
    private Integer powerNumA = null;//	A相电能	(度)	非必填
    private Integer powerNumB = null;//	B相电能	(度)	非必填
    private Integer powerNumC = null;//	C相电能	(度)	非必填
    private Integer powerTotalNum = null;//	三相总电能	(度)	非必填
    private Integer generateNum = null;//	三相实际总电能	(度)	非必填
    private Float oilNum = null;//	用油量	小数（L）	非必填

}
