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

    public static final String IP_PORT_SEPARATOR = ":";
    public static final int IP_PORT_SUFFIX = 0x00;
    private static final String SEPARATOR = " ";
    //下标长度等常量
    public static final int INDEX_ADDRESS = 5;
    public static final int INDEX_COMMAND = 6;
    public static final int INDEX_DATA_START = 7;
    public static final int INDEX_STATE = 0;
    public static final int LENGTH_HEADER = 4;
    public static final int LENGTH_ID = 8;
    public static final int LENGTH_MIN = 8;
    //状态常量
    public static final int RES_HEARTBEAT_DATA = 0x00;
    public static final int RES_STATE_OK = 0x00;
    public static final int RES_OPEN_ALL_OK = 0x00;
    public static final int RES_CHANGE_PSW_OK = 0x00;
    public static final int RES_CHANGE_PORT_OK = 0x00;

    //*控制部分*/
    // 0x79 二维码数据包,商家提供的协议中并没有,实为自己添加(格式:2(格子号,数字)+6(密码,数字))
    public static final Integer CMD_QR_CODE = 0x79;
    // 0x79 快捷下单,商家提供的协议中并没有,实为自己添加(数据域为token)
    public static final Integer CMD_QUICK_ORDER = 0x78;

    //0x80 心跳包
    public static final Integer CMD_HEARTBEAT = 0x80;
    //0x81 获取设备 ID
    public static final Integer CMD_DEVICE_ID = 0x81;
    //0x82 开锁
    public static final Integer CMD_OPEN = 0x82;
    //0x83 读门状态
    public static final Integer CMD_DOOR_STATE = 0x83;
    //0x84 查询所有状态
    public static final Integer CMD_ALL_STATE = 0x84;
    //0x85 主动上传门状态变化
    public static final Integer CMD_AUTO_UPLOAD = 0x85;
    //0x86 开全部锁
    public static final Integer CMD_OPEN_ALL = 0x86;


    /*设置部分*/
    //0x91 设置  更改配置的密码 用于过滤非法更改配置的请求
    public static final Integer CMD_CHANGE_PASSWORD = 0x91;


    static {
        //默认前缀
        // 起始符:四字节，默认为“NBSE”，用户可通过配置工具修改
        header.add(0x4e);
        header.add(0x42);
        header.add(0x53);
        header.add(0x45);
    }

    // 状态字节 0x00 表示执行正确，其它数 值表示执行错误。
    // 校验字节:从帧起始符到数据域最后一个字节逐字节的异或(XOR)值
    // 帧长度:一个字节，为从起始到校验字节的字节数，取值范围为 0x08 ~ 0xFF。
    // 注意数据域长度可以为0
    private byte[] body;

    public GmsTcpPacket(Integer address, Integer cmd, Integer... data) {
        this.address = address;
        this.command = cmd;
        List<Integer> tempData = new ArrayList<>();
        for (Integer d : data) {
            tempData.add(d);
        }
        this.data = tempData;
    }

    public GmsTcpPacket(Integer address, Integer cmd, byte... data) {
        this.address = address;
        this.command = cmd;
        List<Integer> tempData = new ArrayList<>();
        for (byte d : data) {
            tempData.add((int) d);
        }
        this.data = tempData;
    }

    public GmsTcpPacket(Integer address, Integer cmd, List<Integer> data) {
        this.address = address;
        this.command = cmd;
        if (data != null && !data.isEmpty()) {
            this.data = data;
        }
    }


    //默认为心跳包
    public GmsTcpPacket(Integer address) {
        this.address = address;
        this.command = CMD_HEARTBEAT;
    }


    public static GmsTcpPacket parse(byte[] header, byte length, byte address, byte cmd, byte[] data, byte check) throws UnsupportedEncodingException {
        GmsTcpPacket lmsPacket = null;
        if (((Byte) length).intValue() != (LENGTH_MIN + data.length) || !headerMatch(header)) {
            logger.error("长度或头部不匹配!");
            return lmsPacket;
        } else {
            List<Integer> fullMessage = new ArrayList<>();
            //头部4byte
            for (Byte b : header) {
                fullMessage.add(b.intValue());
            }
            //长度 1byte
            fullMessage.add(((Byte) length).intValue());
            //地址 1byte
            fullMessage.add(((Byte) address).intValue());
            //命令 1byte
            fullMessage.add(((Byte) cmd).intValue());
            //数据位 n byte
            for (Byte b : data) {
                fullMessage.add(b.intValue());
            }
            //校验位
            fullMessage.add(((Byte) check).intValue());
            if (checkEveryByte(fullMessage) && headerMatch(fullMessage) && checkXOR(fullMessage)) {
                List<Integer> tempData = null;
                if (fullMessage.size() > LENGTH_MIN) {
                    tempData = fullMessage.subList(INDEX_DATA_START, fullMessage.size() - 1);
                }
                lmsPacket = new GmsTcpPacket(fullMessage.get(INDEX_ADDRESS), fullMessage.get(INDEX_COMMAND), tempData);
            } else {
                String content = "";
                for (Integer integer : fullMessage) {
                    content += Integer.toString(integer, 16) + " ";
                }
                logger.info("无法解析的包[" + content.trim() + "]");
            }
        }
        return lmsPacket;
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
            if (header.get(i) != message.get(i)) {
                logger.info("头部校验不通过!");
                return false;
            }
        }
        return true;
    }

    static boolean headerMatch(byte[] message) {
        for (int i = 0; i < LENGTH_HEADER; i++) {
            if (header.get(i) != ((Byte) message[i]).intValue()) {
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
        Integer calculateCheck = getCheckXOR(beforeCheck);
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
        byte[] resultByte = new byte[LENGTH_MIN + (this.data == null || this.data.isEmpty() ? 0 : this.data.size())];
        int index = 0;
        for (Integer b : header) {
            resultByte[index++] = b.byteValue();
        }
        beforeCheck.addAll(header);
        resultByte[index++] = this.length.byteValue();
        beforeCheck.add(this.length);
        resultByte[index++] = this.address.byteValue();
        beforeCheck.add(this.address);
        resultByte[index++] = this.command.byteValue();
        beforeCheck.add(this.command);
        if (this.data != null && this.data.size() > 0) {
            for (Integer b : this.data) {
                resultByte[index++] = b.byteValue();
            }
            beforeCheck.addAll(this.data);
        }
        this.check = getCheckXOR(beforeCheck);
        resultByte[index] = this.check.byteValue();
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
    private static Integer getCheckXOR(List<Integer> beforeCheck) {
        int firstByte = beforeCheck.get(0);
        for (int i = 1; i < beforeCheck.size(); i++) {
            firstByte = firstByte ^ beforeCheck.get(i);
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

    public List<Integer> getHeader() {
        return header;
    }

    public int getLength() {
        return length;
    }

    public int getAddress() {
        return address;
    }

    public int getCommand() {
        return command;
    }

    public List<Integer> getData() {
        return data;
    }


    public int getCheck() {
        return check;
    }

    private Integer address;
    private Integer command;
    private List<Integer> data;
    private Integer check;

    public boolean isHeartbeat() {
        if (this.command.byteValue() == CMD_HEARTBEAT.byteValue()) {
            return true;
        }
        return false;
    }


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
