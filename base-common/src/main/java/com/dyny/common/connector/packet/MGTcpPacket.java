package com.dyny.common.connector.packet;

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
    private byte headerByte = 0;

    //2.类型
    private byte typeByte = 0;

    //3.属性
    private byte propertyByte = 0;

    //4.长度2位
    private byte[] lengthBytes2 = null;

    //5.产品序列号
    private byte[] prodSerialBytes6 = null;

    //6.荷载
    private byte[] payloadBytes = null;

    //7.帧序列号,2位
    private byte[] frameSerial2 = null;

    //8.CRC校验位,长度固定2位
    private byte[] crcCheck2 = null;

    //9.尾部,固定一位
    private byte tailByte = 0;


    private Integer[] typeOptions = {0x01, 0x02};


    public static MGTcpPacket decodeMGTcpPacket(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext
            channelContext) {
        MGTcpPacket mgTcpPacket = new MGTcpPacket();
        //1.头部
        mgTcpPacket.setHeaderByte(buffer.get());
        byte headerByte = buffer.get();

        //2.类型
        byte typeByte = buffer.get();

        //3.属性
        byte propertyByte = buffer.get();

        //4.长度2位
        byte[] lengthBytes2 = new byte[2];
        buffer.get(lengthBytes2);

        //5.产品序列号
        byte[] prodSerialBytes6 = new byte[6];
        buffer.get(prodSerialBytes6);

        //6.荷载
        BigInteger payloadLength = new BigInteger(lengthBytes2);
        byte[] payloadBytes = new byte[payloadLength.intValue()];
        buffer.get(payloadBytes);

        //7.帧序列号,2位
        byte[] frameSerial2 = new byte[2];
        buffer.get(frameSerial2);

        //8.CRC校验位,长度固定2位
        byte[] crcCheck2 = new byte[2];
        buffer.get(crcCheck2);

        //9.尾部,固定一位
        byte tailByte = buffer.get();


        return null;

    }
}
