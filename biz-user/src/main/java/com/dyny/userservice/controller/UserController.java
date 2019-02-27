package com.dyny.userservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyny.userservice.api.RegionService;
import com.dyny.utils.BaseController;
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
    RegionService regionService;

    @RequestMapping("/test")
    public String test() {
        return "user-service," + regionService.testRegion();
    }


    @RequestMapping("/login")
    public String login(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("age", 18);
        jsonObject.put("gender", 1);
        return getSuccessResult(jsonObject);
    }
}
