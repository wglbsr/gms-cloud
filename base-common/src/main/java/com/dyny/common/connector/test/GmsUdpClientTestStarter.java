package com.dyny.common.connector.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.udp.UdpClient;
import org.tio.core.udp.UdpClientConf;

/**
 * @Auther: lane
 * @Date: 2019-05-03 15:54
 * @Description:
 * @Version 1.0.0
 */
public class GmsUdpClientTestStarter {
    private static Logger logger = LoggerFactory.getLogger(GmsUdpClientTestStarter.class);


    public static void main(String[] args) {
        UdpClientConf udpClientConf = new UdpClientConf("127.0.0.1", 3000, 5000);
        UdpClient udpClient = new UdpClient(udpClientConf);
        udpClient.start();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            String str = i + "、" + "udp message";
            udpClient.send(str.getBytes());
        }
        long end = System.currentTimeMillis();
        long iv = end - start;
        logger.info("耗时:" + iv + "ms");
    }
}
