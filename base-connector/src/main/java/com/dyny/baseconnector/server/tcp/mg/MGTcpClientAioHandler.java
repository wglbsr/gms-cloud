package com.dyny.baseconnector.server.tcp.mg;

import com.dyny.common.utils.Utils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import javax.validation.Payload;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;

/**
 * @Auther: lane
 * @Date: 2018-12-26 13:55
 * @Description:
 * @Version 1.0.0
 */
public class MGTcpClientAioHandler implements ClientAioHandler {
    private static Logger logger = LoggerFactory.getLogger(MGTcpClientAioHandler.class);

    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        return MGTcpPacket.decodeMGTcpPacket(buffer, limit, position, readableLength, channelContext);

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


    /**
     * @return void
     * @Author wanggl(lane)
     * @Description //TODO //平台在应答[统计型]数据帧时，帧流水号必须和[统计型]数据帧的流水号一致
     * @Date 11:51 2019-08-27
     * @Param [packet, channelContext]
     **/
    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws InterruptedException {
        MGTcpPacket mgTcpPacket = (MGTcpPacket) packet;
        byte type = mgTcpPacket.getTypeByte1();
        switch (type) {
            //        0x11	心跳帧
            case MGTcpPacket.TYPE_HEARTBEAT:


                //        0x13	动态型数据帧
            case MGTcpPacket.TYPE_DYNAMIC:


                //        0x14	参数设置帧
            case MGTcpPacket.TYPE_PROS_SETTING:


                //        0x15	统计型数据帧
            case MGTcpPacket.TYPE_STATISTIC:


                //        0x16	时间帧
            case MGTcpPacket.TYPE_TIME:
                //回复时间
                MGTcpPacket response = new MGTcpPacket(mgTcpPacket.getTypeByte1(), (byte) 0x00,
                        mgTcpPacket.getProdSerialBytes6(), getTimeRes(), mgTcpPacket.getFrameSerial2());
                Tio.send(channelContext, response);
            default:
                break;

        }
    }

    private static byte[] getTimeRes() {
        LocalDateTime now = LocalDateTime.now();
        short year = (short) now.getYear();
        byte[] yearBytes = Utils.Byte.shortToBytes(year);
        byte[] result = {0x01, 0x66, 0x00, 0x1C, yearBytes[0], yearBytes[1],
                (byte) now.getMonthValue(),
                (byte) now.getDayOfMonth(),
                (byte) now.getHour(),
                (byte) now.getMinute(),
                (byte) now.getSecond()};
        return result;
    }


    @Override
    public Packet heartbeatPacket(ChannelContext channelContext) {
        return null;
    }
}
