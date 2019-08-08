package com.dyny.baseconnector;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class BaseConnectorApplicationTests {

//	@Test
//	public void contextLoads() {
//	}

    public static void main(String[] args) {
//        System.out.println(test);
        byte[] test = {0x00, 0x00a, 0x012, 0x002};
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < test.length; i++) {

            stringBuilder.append(test[i]+"");

        }
        System.out.println(HexUtils.toHexString(test));
    }

}
