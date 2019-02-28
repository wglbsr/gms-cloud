package com.dyny.gateway;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @Auther: lane
 * @Date: 2019-02-28 13:52
 * @Description:
 * @Version 1.0.0
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println(Base64.getEncoder().encodeToString("usernamepassword123123".getBytes()));
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        System.out.println(timestamp.toString());
    }
}
