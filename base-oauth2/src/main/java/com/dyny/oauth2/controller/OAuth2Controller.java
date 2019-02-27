package com.dyny.oauth2.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-27 09:46
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/base-oauth2")
public class OAuth2Controller {

    @RequestMapping("/token")
    public String token(){
        return "";
    }

    @RequestMapping("/authorize")
    public String auth(){
        return "";
    }
}
