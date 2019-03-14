package com.dyny.baseredis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseRedisApplicationTests {

    @Test
    public void contextLoads() {
        String s1 = "hello";
        String s2 = "he" + new String("llo");
        String s3 = new String("hello");
        System.out.println(s1==s2);
        System.out.println(s2==s3);

    }

}
