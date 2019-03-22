package com.dyny.baseconnector.tcp;

import com.dyny.baseconnector.tcp.packet.TcpPacket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.core.maintain.ClientNodes;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @Author wanggl(lane)
 * @Description //TODO
 * @Date 13:58 2018-12-26
 * @Param
 * @return
 **/
public class TcpClientTestHandler implements ClientAioHandler {
    public static Log logger = LogFactory.getLog(TcpClientTestHandler.class);

    /**
     * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
     * 总的消息结构：消息头 + 消息体
     * 消息头结构：    4个字节，存储消息体的长度
     * 消息体结构：   对象的json串的byte[]
     */
    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        //ip + ":" + port
        ClientNodes.getKey(channelContext);

        //提醒：buffer的开始位置并不一定是0，应用需要从buffer.position()开始读取数据
        //收到的数据组不了业务包，则返回null以告诉框架数据不够
        if (readableLength < TcpPacket.LENGTH_MIN) {
            return null;
        }
        byte[] headerBytes = new byte[TcpPacket.LENGTH_HEADER];
        //读取消息体的长度
        buffer.get(headerBytes, 0, headerBytes.length);
        Byte lengthByte = buffer.get();
        Integer fullPackLength = lengthByte.intValue();
        //数据不正确，则抛出AioDecodeException异常
        if (fullPackLength < 0) {
            throw new AioDecodeException("fullPack length [" + fullPackLength + "] is not right, remote:" + channelContext.getClientNode());
        }
        //除去头部和长度后的长度
        int bodyLength = fullPackLength - TcpPacket.LENGTH_HEADER - 1;
        //收到的数据是否足够组包
        // 不够消息体长度(剩下的buffer组不了消息体)
        if (readableLength - bodyLength < 0) {
            return null;
        } else //组包成功
        {
            TcpPacket lmsPacket = null;
            if (fullPackLength > 0) {
                byte address = buffer.get();
                byte cmd = buffer.get();
                byte[] data = new byte[0];
                int dataLength = fullPackLength - TcpPacket.LENGTH_MIN;
                if (dataLength > 0) {
                    data = new byte[dataLength];
                    buffer.get(data);
                }
                byte check = buffer.get();
                try {
                    lmsPacket = TcpPacket.parse(headerBytes, lengthByte, address, cmd, data, check);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return lmsPacket;
        }
    }

    /**
     * 编码：把业务消息包编码为可以发送的ByteBuffer
     * 总的消息结构：消息头 + 消息体
     * 消息头结构：    4个字节，存储消息体的长度
     * 消息体结构：   对象的json串的byte[]
     */
    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        TcpPacket lmsPacket = (TcpPacket) packet;
        byte[] fullPack = lmsPacket.getFullPack();
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
     * 处理消息
     */
    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        TcpPacket lmsPacket = (TcpPacket) packet;
        if (lmsPacket.getCommand() == TcpPacket.CMD_DEVICE_ID.byteValue()) {
            TcpPacket res = new TcpPacket(0x00, TcpPacket.CMD_DEVICE_ID, 0, 0, 0, 0, 1, 0, 0, 1);
            logger.info("收到[获取设备id]指令!");
            Tio.send(channelContext, res);
            return;
        }

        if (lmsPacket.getCommand() == TcpPacket.CMD_HEARTBEAT.byteValue()) {
            logger.info("收到[心跳包]");
            return;
        }
        if (lmsPacket.getCommand() == TcpPacket.CMD_OPEN_ALL.byteValue()) {
            logger.info("收到[打开所有锁]指令!");
            return;
        }


        if (lmsPacket.getCommand() == TcpPacket.CMD_OPEN.byteValue()) {
            logger.info("收到[打开某个锁]指令!");
            return;
        }


        if (lmsPacket.getCommand() == TcpPacket.CMD_DOOR_STATE.byteValue()) {
            logger.info("收到[获取某个锁状态]指令!");
            return;
        }


        if (lmsPacket.getCommand() == TcpPacket.CMD_ALL_STATE.byteValue()) {
            logger.info("收到[获取所有锁状态]指令!");
            return;
        }
    }

    @Override
    public Packet heartbeatPacket(ChannelContext channelContext) {
        return null;
    }

}