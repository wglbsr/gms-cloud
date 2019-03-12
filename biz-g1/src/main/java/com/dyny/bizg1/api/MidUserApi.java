package com.dyny.bizg1.api;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: lane
 * @Date: 2019-03-12 15:32
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("mid-user")
public interface MidUserApi {
//    @RequestMapping("/create")
//    String create(@RequestParam("username") String username,
//                  @RequestParam("password") String password,
//                  @RequestParam("level") int level,
//                  @RequestParam("nickname") String nickname,
//                  @RequestParam("description") String description);
}
