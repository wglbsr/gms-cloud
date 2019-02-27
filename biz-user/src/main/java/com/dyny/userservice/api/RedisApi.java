package com.dyny.userservice.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: lane
 * @Date: 2019-02-27 16:06
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("base-redis")
public interface RedisApi {

    @RequestMapping("/redis/set")
    String set(String key, String value);

    @RequestMapping("/redis/get")
    String get(String key);
}
