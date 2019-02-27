package com.dyny.gateway.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: lane
 * @Date: 2019-02-27 10:52
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("base-cache")
public interface CacheApi {
    @RequestMapping("/cache/set")
    String set(String key, String value);

    @RequestMapping("/cache/get")
    String get(String key);
}
