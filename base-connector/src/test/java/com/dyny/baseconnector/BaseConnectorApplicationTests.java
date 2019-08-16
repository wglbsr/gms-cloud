package com.dyny.baseconnector;

import com.dyny.common.utils.Utils;
import org.apache.tomcat.util.buf.HexUtils;

import java.nio.ByteBuffer;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class BaseConnectorApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

    public static void main(String[] args) {
//        System.out.println(test);
//        byte[] test = {0x00, 0x00a, 0x012, 0x002};
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < test.length; i++) {
//
//            stringBuilder.append(test[i]+"");
//
//        }
//        System.out.println(HexUtils.toHexString(test));

        byte[] valueByte = {0x42, (byte) 0xE8, (byte) 0xD1, (byte) 0xEE};
//        Float testFloat = 0.123456f;
//        System.out.println(HexUtils.toHexString(test));
        System.out.println(ByteBuffer.wrap(valueByte).getFloat());
//        System.out.println(Integer.toString(Float.floatToIntBits(0x42E8D1EE),16));
        System.out.println(byte2float(valueByte,0));



    }

    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 3];
        l &= 0xff;
        l |= ((long) b[index + 2] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 1] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 0] << 24);
        return Float.intBitsToFloat(l);
    }

}
