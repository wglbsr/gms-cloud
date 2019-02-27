package com.dyny.gateway.controller;

import com.dyny.utils.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-27 10:04
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    @RequestMapping("/login")
    public String login(String username,String password){
        return getErrorMsg("错误的账号或密码!");
    }

}
