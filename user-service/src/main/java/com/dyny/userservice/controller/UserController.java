package com.dyny.userservice.controller;

import com.dyny.userservice.api.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-22 11:27
 * @Description:
 * @Version 1.0.0
 */
@RequestMapping("/user-service")
@RestController
public class UserController {
    @Autowired
    RegionService regionService;
    @RequestMapping("/test")
    public String test(){
        return "user-service,"+regionService.testRegion();
    }
}
