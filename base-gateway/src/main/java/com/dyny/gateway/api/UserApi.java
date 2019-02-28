package com.dyny.gateway.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: lane
 * @Date: 2019-02-28 09:00
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("/service-user")
public interface UserApi {
    @RequestMapping("/user/login")
    String login(String username, String password);
}
