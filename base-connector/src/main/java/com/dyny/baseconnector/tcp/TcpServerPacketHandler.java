package com.dyny.baseconnector.tcp;

import com.dyny.baseconnector.tcp.packet.TcpPacket;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.Node;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @Author wanggl(lane)
 * @Description //TODO 完成之后应该放在common模块中
 * @Date 10:50 2019-03-22
 **/
public class TcpServerPacketHandler implements ServerAioHandler {
    private static Log logger = LogFactory.getLog(TcpClientTestStarter.class);


    @Override
    public TcpPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
        if (readableLength < 6) {
            return null;
        }
        byte[] headerBytes = new byte[TcpPacket.LENGTH_HEADER];
        //读取消息体的长度
        buffer.get(headerBytes, 0, headerBytes.length);
        logger.info("header[" + Hex.encodeHexString(headerBytes) + "]");
        //分为普通包和二维码包,直接为二维码图片对应的字符,因此也没有头部
        if (TcpPacket.isHeaderMatch(headerBytes)) {
            logger.info("普通包,有格式");
            return this.getNormalPacket(buffer, headerBytes, limit, position, readableLength, channelContext);
        } else {
            return null;
        }
    }


    /**
     * @return com.yniot.lms.socket.packet.TcpPacket
     * @Author wanggl(lane)
     * @Description //TODO 普通解包
     * @Date 10:47 2019-01-14
     * @Param [buffer, headerBytes, limit, position, readableLength, channelContext]
     **/
    private TcpPacket getNormalPacket(ByteBuffer buffer, byte[] headerBytes, int limit, int position, int readableLength, ChannelContext channelContext) throws AioDecodeException {
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
        int bodyLength = fullPackLength - TcpPacket.LENGTH_HEADER - 1;
        //收到的数据是否足够组包
        // 不够消息体长度(剩下的buffer组不了消息体)
        if (readableLength - bodyLength < 0) {
            logger.info("长度不够");
            return null;
        } else //组包成功
        {
            TcpPacket tcpPacket = null;
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
                    tcpPacket = TcpPacket.parse(headerBytes, lengthByte, address, cmd, data, check);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return tcpPacket;
        }
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        TcpPacket tcpPacket = (TcpPacket) packet;
        byte[] fullPack = tcpPacket.getFullPack();
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
    public void handler(Packet packet, ChannelContext channelContext) {
        TcpPacket tcpPacket = (TcpPacket) packet;
        String wardrobeId = channelContext.getBsId();
        //是否有绑定业务id,没有则发送获取设备id命令
        //没有柜子id则不做后续操作
        if (StringUtils.isEmpty(wardrobeId)) {
            //1设备id
            logger.info("没有绑定id!");
            return;
        }

        logger.info("full packet[" + tcpPacket.getFullContent(true) + "]");

    }


}
