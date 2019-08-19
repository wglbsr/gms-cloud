package com.dyny.baseconnector.server.tcp.mg;

import com.dyny.common.utils.Utils;
import com.google.common.io.ByteSource;
import com.google.common.primitives.Bytes;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.binary.Hex;
import org.apache.ibatis.javassist.bytecode.ByteArray;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.tio.core.utils.ByteBufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.BitSet;

/**
 * @Auther: wglbs
 * @Date: 2019/8/19 09:28
 * @Description:
 * @Version 1.0.0
 */
@Getter
@Setter
public class MGWSPacket {

    private int frameType;
    private int frameProperty;
    private String prodSerial;
    private int frameSerial;
    private String payload;

    public MGWSPacket() {
    }


    public MGWSPacket(int frameType, int frameProperty, byte[] prodSerial6, byte[] frameSerial2, byte[] payload0) {
        this.frameType = frameType;
        this.frameProperty = frameProperty;
        this.prodSerial = Hex.encodeHexString(prodSerial6);
        this.frameSerial = Utils.Byte.bytes2Int2(frameSerial2);
        this.payload = Hex.encodeHexString(payload0);
    }

    public static MGWSPacket parsePacket(MGTcpPacket mgTcpPacket) {
        return new MGWSPacket(mgTcpPacket.getTypeByte1(), mgTcpPacket.getPropertyByte1(), mgTcpPacket.getProdSerialBytes6(), mgTcpPacket.getFrameSerial2(), mgTcpPacket.getPayloadBytes0());
    }

}
