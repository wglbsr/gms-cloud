package com.dyny.userservice.controller;

import com.dyny.common.utils.BaseController;
import com.dyny.userservice.api.RegionApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-22 11:27
 * @Description:
 * @Version 1.0.0
 */
@RequestMapping("/user")
@RestController
public class UserController extends BaseController {
    @Autowired
    RegionApi regionApi;

    @RequestMapping("/test")
    public String test() {
        return "user-service," + regionApi.testRegion();
    }


}
