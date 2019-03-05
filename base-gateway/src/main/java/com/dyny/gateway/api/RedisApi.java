package com.dyny.gateway.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: lane
 * @Date: 2019-03-04 15:14
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("base-redis")
public interface RedisApi {

    @RequestMapping("/redis/get")
    public String get(@RequestParam("key") String key);

    @RequestMapping("/redis/set")
    public String set(@RequestParam("key") String key, @RequestParam("value") String value,
                      @RequestParam(value = "timeout", required = false, defaultValue = "0") int timeoutMin);

    @RequestMapping("/redis/refresh")
    public String refresh(@RequestParam("key") String key,
                          @RequestParam("timeout") int timeoutMin);
}
