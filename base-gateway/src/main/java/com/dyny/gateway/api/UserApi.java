package com.dyny.gateway.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: lane
 * @Date: 2019-02-27 10:38
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("service-user")
public interface UserApi {

    @RequestMapping("/user/login")
    String testRegion();
}
