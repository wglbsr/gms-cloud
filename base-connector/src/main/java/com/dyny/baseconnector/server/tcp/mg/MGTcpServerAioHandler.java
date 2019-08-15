package com.dyny.baseconnector.server.tcp.mg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * @Auther: wglbs
 * @Date: 2019/8/7 09:17
 * @Description:
 * @Version 1.0.0
 */
public class MGTcpServerAioHandler implements ServerAioHandler {
    private static Logger logger = LoggerFactory.getLogger(MGTcpServerAioHandler.class);


    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
//        return MGTcpPacket.decodeMGTcpPacket(buffer, limit, position, readableLength, channelContext);
        //1.头部,固定
        byte headerByte = buffer.get();
        if (!MGTcpPacket.isHeaderMatch(headerByte)) {
            logger.info("头部不匹配![" + headerByte + "]");
            return null;
        }
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
        if (500 < payloadLength.intValue()) {
            logger.info("有效荷载长度大于500!");
            return null;
        }

        //7.帧序列号,2位
        byte[] frameSerial2 = new byte[2];
        buffer.get(frameSerial2);

        //获取载荷
        byte[] payloadBytes0 = new byte[payloadLength.intValue()];
        buffer.get(payloadBytes0);



        //8.CRC校验位,长度固定2位
        byte[] crcCheck2 = new byte[2];
        buffer.get(crcCheck2);

        //9.尾部,固定一位
        byte tailByte = buffer.get();
        if (!MGTcpPacket.isTailMatch(tailByte)) {
            logger.info("尾部不匹配![" + headerByte + "]");
            return null;
        }
        MGTcpPacket mgTcpPacket = new MGTcpPacket(typeByte, propertyByte, prodSerialBytes6, payloadBytes0, frameSerial2);
        if (mgTcpPacket.isCrcMatch(crcCheck2)) {
            return mgTcpPacket;
        }
        return null;
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        MGTcpPacket mgTcpPacket = (MGTcpPacket) packet;
        byte[] fullPack = mgTcpPacket.getFullPacket();
        int packLen = 0;
        if (fullPack != null) {
            packLen = fullPack.length;
        }
        int allLen = packLen;
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        //设置字节序
        buffer.order(groupContext.getByteOrder());
        //写入消息体
        if (fullPack != null) {
            buffer.put(fullPack);
        }
        return buffer;
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        MGTcpPacket mgTcpPacket = (MGTcpPacket) packet;
        if (mgTcpPacket != null) {
             logger.info("解码成功!");
        }

    }
}
