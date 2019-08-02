package com.dyny.common.connector.packet;

import com.dyny.common.utils.Utils;
import lombok.Data;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * @Author wanggl(lane)
 * @Description //TODO
 * @Date 10:34 2019/7/9
 * @Param
 * @return
 **/
@Data
public class MGTcpPacket extends Packet {


    //1.头部
    private byte headerByte1 = (byte) 0xBC;

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
    private byte tailByte1 = (byte) 0xBC;


    //0.fullPacket
    private byte[] fullPacket = null;

    MGTcpPacket(byte typeByte1, byte propertyByte1, byte[] prodSerialBytes6, byte[] payloadBytes0, byte[] frameSerial2) {
        this.setTypeByte1(typeByte1);
        this.setPropertyByte1(propertyByte1);
        this.setProdSerialBytes6(prodSerialBytes6);
        this.setFrameSerial2(frameSerial2);
        this.setPayloadBytes0(payloadBytes0);
        this.setCrcCheck2();

        //设置fullPacket
        byte[] fullBytes = new byte[this.payloadBytes0.length + 16];
        ByteBuffer bb = ByteBuffer.wrap(fullBytes);
        bb.put(this.headerByte1);
        bb.put(this.typeByte1);
        bb.put(this.propertyByte1);
        bb.put(this.lengthBytes2);
        bb.put(this.prodSerialBytes6);
        bb.put(this.frameSerial2);
        bb.put(this.payloadBytes0);
        bb.put(this.crcCheck2);
        bb.put(this.tailByte1);
        this.setFullPacket(bb.array());
    }
//
//    MGTcpPacket() {
//
//    }


    public void setPayloadBytes0(byte[] payloadBytes0) {
        this.payloadBytes0 = payloadBytes0;
        byte[] size = Utils.Byte.int2bytes(payloadBytes0.length);
        this.setLengthBytes2(ByteBuffer.wrap(size, 0, 2).array());
    }

    private void setCrcCheck2() {

    }


    private boolean isHeaderMatch(byte header) {
        return header == this.headerByte1;
    }

    private boolean isTailMatch(byte tailByte1) {
        return tailByte1 == this.tailByte1;
    }


    private boolean isCrcMatch(byte[] crcCheck2) {
        for (int index = 0; index < crcCheck2.length; index++) {
            if (crcCheck2[index] != this.crcCheck2[index]) {
                return false;
            }
        }
        return true;
    }


    public boolean isAllMatch(byte headerByte1, byte tailByte1, byte[] crcCheck2) {
        return this.isHeaderMatch(headerByte1) && this.isTailMatch(tailByte1) && isCrcMatch(crcCheck2);
    }


    public static MGTcpPacket decodeMGTcpPacket(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext
            channelContext) {
        //1.头部,固定
        byte headerByte = buffer.get();
        //2.类型
        //0x11 心跳帧
        //0x13 动态型数据帧
        //0x14 参数设置帧
        //0x15 统计型数据帧
        //0x16 时间帧
        byte typeByte = buffer.get();

        //3.属性,预留
        byte propertyByte = buffer.get();

        //4.长度2位 ≤500	有效载荷区的数据长度
        byte[] lengthBytes2 = new byte[2];
        buffer.get(lengthBytes2);

        //5.产品序列号
        byte[] prodSerialBytes6 = new byte[6];
        buffer.get(prodSerialBytes6);

        //6.荷载
        BigInteger payloadLength = new BigInteger(lengthBytes2);
        byte[] payloadBytes0 = new byte[payloadLength.intValue()];
        buffer.get(payloadBytes0);

        //7.帧序列号,2位
        byte[] frameSerial2 = new byte[2];
        buffer.get(frameSerial2);

        //8.CRC校验位,长度固定2位
        byte[] crcCheck2 = new byte[2];
        buffer.get(crcCheck2);

        //9.尾部,固定一位
        byte tailByte = buffer.get();

        MGTcpPacket mgTcpPacket = new MGTcpPacket(typeByte, propertyByte, prodSerialBytes6, payloadBytes0, frameSerial2);


        return mgTcpPacket.isAllMatch(headerByte, tailByte, crcCheck2) ? mgTcpPacket : null;

    }
}
