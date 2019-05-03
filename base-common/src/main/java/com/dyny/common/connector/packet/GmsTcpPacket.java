package com.dyny.common.connector.packet;

import com.dyny.common.utils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tio.core.intf.Packet;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lane
 * @Date: 2018-12-26 13:53
 * @Description:
 * @Version 1.0.0
 * <p>
 * 注意通道号port由0x01开始
 */
public class GmsTcpPacket extends Packet {
    public static Log logger = LogFactory.getLog(GmsTcpPacket.class);
    private static List<Integer> header = new ArrayList<>();

    private static final String SEPARATOR = " ";
    //下标长度等常量
    public static final int INDEX_COMMAND = 2;
    public static final int INDEX_DATA_START = 3;
    public static final int LENGTH_HEADER = 1;
    public static final int LENGTH_MIN = 8;
    //状态常量

    //0x80 心跳包
    public static final Integer CMD_HEARTBEAT = 0x80;
    public static final Integer TAIL = 0x16;

    //0x81 获取设备 ID

    static {
        header.add(0x84);
    }

    private byte[] body;

    public GmsTcpPacket(Integer cmd, Integer... data) {
        this.command = cmd;
        List<Integer> tempData = new ArrayList<>();
        for (Integer d : data) {
            tempData.add(d);
        }
        this.data = tempData;
    }

    public GmsTcpPacket(Integer cmd, byte... data) {
        this.command = cmd;
        List<Integer> tempData = new ArrayList<>();
        for (byte d : data) {
            tempData.add((int) d);
        }
        this.data = tempData;
    }

    public GmsTcpPacket(Integer cmd, List<Integer> data) {
        this.command = cmd;
        if (data != null && !data.isEmpty()) {
            this.data = data;
        }
    }


    //头 长度 命令          数据位            校验   尾部
    //84  07  ff     19 01 00 10 ff ff        27    16
    public static GmsTcpPacket parse(byte[] header, byte length, byte cmd, byte[] data, byte check) throws UnsupportedEncodingException {
        GmsTcpPacket gmsPacket = null;
        if (((Byte) length).intValue() != (1 + data.length) || !headerMatch(header)) {
            logger.error("长度或头部不匹配!");
            return gmsPacket;
        } else {
            List<Integer> fullMessage = new ArrayList<>();
            //头部1byte
            for (Byte b : header) {
                fullMessage.add(b.intValue());
            }
            //长度 1byte
            fullMessage.add(((Byte) length).intValue());
            //命令 1byte
            fullMessage.add(((Byte) cmd).intValue());
            //数据位 n byte
            for (Byte b : data) {
                fullMessage.add(b.intValue());
            }
            //校验位
            fullMessage.add(((Byte) check).intValue());
            fullMessage.add(TAIL);
            if (checkEveryByte(fullMessage) && headerMatch(fullMessage)) {
                List<Integer> tempData = null;
                if (fullMessage.size() > LENGTH_MIN) {
                    tempData = fullMessage.subList(INDEX_DATA_START, fullMessage.size() - 2);
                }
                gmsPacket = new GmsTcpPacket(fullMessage.get(INDEX_COMMAND), tempData);
            } else {
                String content = "";
                for (Integer integer : fullMessage) {
                    content += Integer.toString(integer, 16) + " ";
                }
                logger.info("无法解析的包[" + content.trim() + "]");
            }
        }
        return gmsPacket;
    }


    //是否超过255  即是否大于1字节
    private static boolean checkEveryByte(List<Integer> message) {
        for (Integer integer : message) {
            if (integer.intValue() > 255) {
                logger.info("帧过长!");
                return false;
            }
        }
        return true;
    }

    //前缀是否符合,意义不大
    static boolean headerMatch(List<Integer> message) {
        for (int i = 0; i < LENGTH_HEADER; i++) {
            if (header.get(i).byteValue() != message.get(i)) {
                logger.info("头部校验不通过!");
                return false;
            }
        }
        return true;
    }

    static boolean headerMatch(byte[] message) {
        for (int i = 0; i < LENGTH_HEADER; i++) {
            if (header.get(i).byteValue() != message[i]) {
                logger.info("头部校验不通过!");
                return false;
            }
        }
        return true;
    }

    //校验码检查
    static boolean checkXOR(List<Integer> message) {
        Integer check = message.get(message.size() - 1);
        List<Integer> beforeCheck = message.subList(0, message.size() - 1);
        Integer calculateCheck = getCheckAdd(beforeCheck).intValue();
        boolean result = calculateCheck.intValue() == check.intValue();
        if (!result) {
            logger.info("校验位检测不通过,正确值[" + calculateCheck.intValue() + "],获得值[" + check.intValue() + "]");
        }
        return result;
    }

    public byte[] getBody() {
        byte[] fullPack = this.getFullPack();
        int bodySize = this.getFullPack().length - LENGTH_HEADER;
        byte[] body = new byte[bodySize];
        for (int i = LENGTH_HEADER; i < fullPack.length; i++) {
            body[i - LENGTH_HEADER] = fullPack[i];
        }
        this.body = body;
        return body;
    }

    public byte[] getFullPack() {
        if (this.data != null) {
            this.length = LENGTH_MIN + this.data.size();
        } else {
            this.length = LENGTH_MIN;
        }
        List<Integer> beforeCheck = new ArrayList<>();
        byte[] resultByte = new byte[5 + (this.data == null || this.data.isEmpty() ? 0 : this.data.size())];
        int index = 0;
        for (Integer b : header) {
            resultByte[index++] = b.byteValue();
        }
        beforeCheck.addAll(header);
        resultByte[index++] = Integer.valueOf(this.data.size()+1).byteValue();
        beforeCheck.add(this.length);
        resultByte[index++] = this.command.byteValue();
        beforeCheck.add(this.command);
        if (this.data != null && this.data.size() > 0) {
            for (Integer b : this.data) {
                resultByte[index++] = b.byteValue();
            }
            beforeCheck.addAll(this.data);
        }
        this.check = getCheckAdd(beforeCheck).intValue();
        resultByte[index++] = this.check.byteValue();
        resultByte[index] = TAIL.byteValue();
        return resultByte;
    }

    /**
     * @return java.lang.String
     * @Author wanggl(lane)
     * @Description //TODO 获取指令内容
     * @Date 14:43 2019-01-03
     * @Param []
     **/
    public String getFullContent(boolean separate) {
        byte[] fullPack = getFullPack();
        String content = "";
        for (Byte b : fullPack) {
            String hexStr = Integer.toHexString(Byte.toUnsignedInt(b));
            String byteStr = Utils.String.autoAppendZero(hexStr, 2);
            content += byteStr + (separate ? SEPARATOR : "");
        }
        return content.trim();
    }


    //0x4E 0x42 0x53 0x45
    private static Byte getCheckAdd(List<Integer> beforeCheck) {
        byte firstByte = 0;
        for (int i = INDEX_COMMAND; i < beforeCheck.size(); i++) {
            firstByte += beforeCheck.get(i).byteValue();
        }
        return firstByte;
    }


    /**
     * @return boolean
     * @Author wanggl(lane)
     * @Description //TODO 判断地址和命令是否一致
     * @Date 09:25 2018-12-28
     * @Param [address, lmsPacket]
     **/
    private boolean isMatch(int cmd) {
        byte thisCmd = (byte) this.getCommand();
        boolean res = thisCmd == (byte) cmd;
        if (!res) {
            logger.info("命令不匹配!thisCmd:[" + thisCmd + "],[" + (byte) cmd + "]");
        }
        return res;
    }


    private Integer length;

    public int getCommand() {
        return command;
    }

    public List<Integer> getData() {
        return data;
    }


    public int getCheck() {
        return check;
    }

    private Integer command;
    private List<Integer> data;
    private Integer check;


    public static boolean isHeaderMatch(byte[] headerData) {
        if (headerData != null && headerData.length == header.size()) {
            for (int i = 0; i < header.size(); i++) {
                if (header.get(i).byteValue() != headerData[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
