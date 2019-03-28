package com.dyny.baseconnector.client.tcp;

import com.dyny.common.packet.GmsTcpPacket;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Node;
import org.tio.core.Tio;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @Auther: lane
 * @Date: 2019-03-22 11:53
 * @Description:
 * @Version 1.0.0
 */
public class GmsTcpClientAioHandler implements ClientAioHandler {
    private static Logger logger = LoggerFactory.getLogger(GmsTcpClientAioHandler.class);

    public GmsTcpClientAioHandler() {

    }


    @Override
    public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        return tcpDecoder(buffer, limit, position, readableLength, channelContext);
    }


    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        return tcpEncoder(packet, groupContext, channelContext);
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        tcpHandler(packet, channelContext);
    }


    /**********************************普通tcp的相关处理逻辑start******************************************/
    private ByteBuffer tcpEncoder(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        GmsTcpPacket gmsTcpPacket = (GmsTcpPacket) packet;
        byte[] fullPack = gmsTcpPacket.getFullPack();
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
     * @return com.yniot.lms.socket.packet.GmsTcpPacket
     * @Author wanggl(lane)
     * @Description //TODO 普通解包
     * @Date 10:47 2019-01-14
     * @Param [buffer, headerBytes, limit, position, readableLength, channelContext]
     **/
    private GmsTcpPacket getNormalPacket(ByteBuffer buffer, byte[] headerBytes, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        //提醒：buffer的开始位置并不一定是0，应用需要从buffer.position()开始读取数据
        //收到的数据组不了业务包，则返回null以告诉框架数据不够
        Byte lengthByte = buffer.get();
        Integer fullPackLength = lengthByte.intValue();
        //数据不正确，则抛出AioDecodeException异常
        Node clientNode = channelContext.getClientNode();
        String ip = clientNode.getIp();
        if (fullPackLength < 0) {
            throw new AioDecodeException("fullPack length [" + fullPackLength + "] is not right, domain:" + ip);
        }
        //除去头部和长度后的长度
        int bodyLength = fullPackLength - GmsTcpPacket.LENGTH_HEADER - 1;
        //收到的数据是否足够组包
        // 不够消息体长度(剩下的buffer组不了消息体)
        if (readableLength - bodyLength < 0) {
            logger.info("长度不够");
            return null;
        } else //组包成功
        {
            GmsTcpPacket gmsTcpPacket = null;
            if (fullPackLength > 0) {
                byte address = buffer.get();
                byte cmd = buffer.get();
                byte[] data = new byte[0];
                int dataLength = fullPackLength - GmsTcpPacket.LENGTH_MIN;
                if (dataLength > 0) {
                    data = new byte[dataLength];
                    buffer.get(data);
                }
                byte check = buffer.get();
                try {
                    gmsTcpPacket = GmsTcpPacket.parse(headerBytes, lengthByte, address, cmd, data, check);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            logger.info(gmsTcpPacket.getFullContent(true));
            return gmsTcpPacket;
        }
    }

    private Packet tcpDecoder(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {

        if (readableLength < 6) {
            return null;
        }
        byte[] headerBytes = new byte[GmsTcpPacket.LENGTH_HEADER];
        //读取消息体的长度
        buffer.get(headerBytes, 0, headerBytes.length);
        logger.info("header[" + Hex.encodeHexString(headerBytes) + "]");
        //分为普通包和二维码包,直接为二维码图片对应的字符,因此也没有头部
        if (GmsTcpPacket.isHeaderMatch(headerBytes)) {
            logger.info("普通包,有格式");
            return this.getNormalPacket(buffer, headerBytes, limit, position, readableLength, channelContext);
        } else {
            return null;
        }
    }

    private void tcpHandler(Packet packet, ChannelContext channelContext) {
        GmsTcpPacket gmsTcpPacket = (GmsTcpPacket) packet;
        logger.info("full packet[" + gmsTcpPacket.getFullContent(true) + "]");
        Tio.send(channelContext, new GmsTcpPacket(0x00, GmsTcpPacket.CMD_OPEN_ALL, 0x00));
    }


    @Override
    public Packet heartbeatPacket(ChannelContext channelContext) {
        return null;
    }
    /*+++++++++++++++++++++++++++++++++++++websocket的相关处理逻辑end+++++++++++++++++++++++++++++++++++++*/

}
