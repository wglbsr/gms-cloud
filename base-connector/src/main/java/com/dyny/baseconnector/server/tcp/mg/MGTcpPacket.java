package com.dyny.baseconnector.server.tcp.mg;

import com.dyny.common.connector.packet.MGDataUnit;
import com.dyny.common.utils.Crc16Util;
import com.dyny.common.utils.GDPayloadUtils;
import com.dyny.common.utils.Utils;
import lombok.Data;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author wanggl(lane)
 * @Description //TODO
 * @Date 10:34 2019/7/9
 * @Param
 * @return
 **/
@Data
public class MGTcpPacket extends Packet {
    private static Logger logger = LoggerFactory.getLogger(MGTcpPacket.class);


    //1.头部

    private static final byte headerByte1 = (byte) 0xBC;

    //2.类型
    private byte typeByte1 = 0;

    //3.属性
    private byte propertyByte1 = 0;

    //4.长度2位
    private byte[] lengthBytes2 = null;

    //5.产品序列号
    private byte[] prodSerialBytes6 = null;

    //6.荷载
    private byte[] payloadBytes0 = null;

    //7.帧序列号,2位
    private byte[] frameSerial2 = null;

    //8.CRC校验位,长度固定2位
    private byte[] crcCheck2 = new byte[2];

    //9.尾部,固定一位
    private static final byte tailByte1 = (byte) 0xBC;


    //0.fullPacket
    private byte[] fullPacket = null;
    private byte[] beforeCrcBytes = null;

    public MGTcpPacket(byte typeByte1, byte propertyByte1, byte[] prodSerialBytes6, byte[] payloadBytes0, byte[] frameSerial2) {
        this.setTypeByte1(typeByte1);
        this.setPropertyByte1(propertyByte1);
        this.setProdSerialBytes6(prodSerialBytes6);
        this.setFrameSerial2(frameSerial2);
        this.setPayloadBytes0(payloadBytes0);
        //设置fullPacket
        ByteBuffer bb = ByteBuffer.allocate(this.payloadBytes0.length + 16);
        bb.put(MGTcpPacket.headerByte1);
        bb.put(this.typeByte1);
        bb.put(this.propertyByte1);
        bb.put(this.lengthBytes2);
        bb.put(this.prodSerialBytes6);
        bb.put(this.frameSerial2);
        bb.put(this.payloadBytes0);
        this.setBeforeCrcBytes(bb.array());
        this.setCrcCheck2();
        bb.put(this.crcCheck2);
        bb.put(MGTcpPacket.tailByte1);
        this.setFullPacket(bb.array());
        this.setDataUnitList();
    }

    public MGTcpPacket(int radix, String typeByte1, String propertyByte1, List<String> prodSerialNum6, List<String> payloadByteList, List<String> frameSerialNum2) {


        new MGTcpPacket(Byte.valueOf(typeByte1, radix),
                Byte.valueOf(propertyByte1, radix),
                list2ByteArray(prodSerialNum6, radix),
                list2ByteArray(payloadByteList, radix),
                list2ByteArray(frameSerialNum2, radix));

    }

    private byte[] list2ByteArray(List<String> stringArray, int radix) {
        byte[] byteArray = new byte[stringArray.size()];
        for (int i = 0; i < stringArray.size(); i++) {
            byteArray[i] = Byte.valueOf(stringArray.get(i), radix);
        }
        return byteArray;
    }
//
//    MGTcpPacket() {
//
//    }


    public void setPayloadBytes0(byte[] payloadBytes0) {
        this.payloadBytes0 = payloadBytes0;
        byte[] size = Utils.Byte.int2bytes(payloadBytes0.length);
        this.setLengthBytes2(ByteUtils.subArray(size, 2, 4));
    }

    private void setCrcCheck2() {
        byte[] crcBytes = Crc16Util.getCrc16(this.beforeCrcBytes);
        if (crcBytes.length >= 2) {
            byte[] temp = new byte[2];
            temp[0] = crcBytes[0];
            temp[1] = crcBytes[1];
            this.setCrcCheck2(crcBytes);
        } else if (crcBytes.length == 1) {
            byte[] temp = new byte[2];
            temp[0] = crcBytes[0];
            this.setCrcCheck2(temp);
        }
    }


    public static boolean isHeaderMatch(byte header) {
        return header == MGTcpPacket.headerByte1;
    }

    public static boolean isTailMatch(byte tailByte1) {
        return tailByte1 == MGTcpPacket.tailByte1;
    }


    public boolean isCrcMatch(byte[] crcCheck2) {
        for (int index = 0; index < crcCheck2.length; index++) {
            if (crcCheck2[index] != this.crcCheck2[index]) {
                return false;
            }
        }
        return true;
    }


    public boolean isAllMatch(byte headerByte1, byte tailByte1, byte[] crcCheck2) {
        return MGTcpPacket.isHeaderMatch(headerByte1) && MGTcpPacket.isTailMatch(tailByte1) && isCrcMatch(crcCheck2);
    }



    private List<MGDataUnit> dataUnitList = null;

    //
    private void setDataUnitList() {
        int size = this.payloadBytes0.length;
        if (size > 0 && size % 12 == 0) {
            return;
        }
        this.dataUnitList = new ArrayList<>();
        for (int i = 0; i * 12 < payloadBytes0.length; i++) {
            int index = i * 12;
            MGDataUnit mgDataUnit = new MGDataUnit(ByteUtils.subArray(payloadBytes0, index, 12));
            dataUnitList.add(mgDataUnit);
        }
    }


//    public float getLatitude(){
//
//    }

    /**************************回复型报文**********************************/


    //1.时间应答报文0x1C006601
    public static MGTcpPacket getTimeResPacket(byte[] frameSerial2, byte[] prodSerialBytes6) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        String year1 = (year + "").substring(0, 2);
        String year2 = (year + "").substring(2, 4);
        Integer month = now.getMonthValue();
        Integer day = now.getDayOfMonth();
        Integer hour = now.getHour();
        Integer minute = now.getMinute();
        Integer second = now.getSecond();
        byte[] data8 = {Integer.valueOf(year1).byteValue(),
                Integer.valueOf(year2).byteValue(),
                month.byteValue(),
                day.byteValue(),
                hour.byteValue(),
                minute.byteValue(),
                second.byteValue(), 0x00};
        MGTcpPacket mgTcpPacket = new MGTcpPacket((byte) 0x14
                , (byte) 0x00, prodSerialBytes6, GDPayloadUtils.combinePayload(0x1C006601, data8), frameSerial2);
        return mgTcpPacket;
    }


    //2.动态型报文发送周期配置0x12340004
    //第五个字节0x55,固定
    //六七个字节为发送周期
    public static MGTcpPacket getDynamicTimePacketRes(byte[] frameSerial2, byte[] prodSerialBytes6, int seconds) {
        byte[] data = {0x55};
        byte[] secondBytes = ByteUtils.subArray(Utils.Byte.int2bytes(seconds), 0, 2);
        MGTcpPacket mgTcpPacket = new MGTcpPacket((byte) 0x14
                , (byte) 0x00, prodSerialBytes6, GDPayloadUtils.combinePayload(0x12340004, 8, ByteUtils.concatenate(data, secondBytes)), frameSerial2);
        return mgTcpPacket;
    }


    //3.心跳报文发送周期配置0x12340005
    //第五个字节0x55,固定
    //六七个字节为发送周期
    public static MGTcpPacket getHeartbeatTimePacketRes(byte[] frameSerial2, byte[] prodSerialBytes6, int seconds) {
        byte[] data = {0x55};
        byte[] secondBytes = ByteUtils.subArray(Utils.Byte.int2bytes(seconds), 0, 2);
        MGTcpPacket mgTcpPacket = new MGTcpPacket((byte) 0x14
                , (byte) 0x00, prodSerialBytes6, GDPayloadUtils.combinePayload(0x12340005, 8, ByteUtils.concatenate(data, secondBytes)), frameSerial2);
        return mgTcpPacket;
    }

    //4.强制休眠0x12340008
    //第五个字节0x55,固定
    public static MGTcpPacket getForceToSleepPacketRes(byte[] frameSerial2, byte[] prodSerialBytes6) {
        byte[] data = {0x55};
        MGTcpPacket mgTcpPacket = new MGTcpPacket((byte) 0x14
                , (byte) 0x00, prodSerialBytes6, GDPayloadUtils.combinePayload(0x12340008, 8, data), frameSerial2);
        return mgTcpPacket;
    }


    //5.远程升级0x12345040
    //第五个字节0x02：升级无线通讯程序，0x03：升级控制器程序
    public static MGTcpPacket getUpgradePacketRes(byte[] frameSerial2, byte[] prodSerialBytes6, int upgradeType) {
        byte[] data = {0x55, (byte) upgradeType};
        MGTcpPacket mgTcpPacket = new MGTcpPacket((byte) 0x14
                , (byte) 0x00, prodSerialBytes6, GDPayloadUtils.combinePayload(0x12345040, 8, data), frameSerial2);
        return mgTcpPacket;
    }

}