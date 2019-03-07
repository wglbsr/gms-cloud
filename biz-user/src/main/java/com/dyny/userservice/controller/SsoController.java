package com.dyny.userservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.BaseController;
import com.dyny.userservice.api.RedisApi;
import com.dyny.userservice.db.entity.User;
import com.dyny.userservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: lane
 * @Date: 2019-03-04 14:33
 * @Description:
 * @Version 1.0.0
 */
//@RestController
@RequestMapping(value = "/sso", produces = {"application/json;charset=UTF-8"})
@Controller
@RefreshScope
public class SsoController extends BaseController {

    @Autowired
    UserService userService;
    @Autowired
    RedisApi redisApi;
    @Value("${tokenTimeoutMin:" + TIME_OUT_TOKEN_MIN + "}")
    private int tokenTimeoutMin;

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userService.login(username, password);
        if (user != null) {
            String token = generateToken(username, password);
            redisApi.set(token, JSONObject.toJSONString(user), tokenTimeoutMin);
            return getLoginResult(token, user);
        } else {
            return getErrorMsg("找不到用户!");
        }
    }


    @RequestMapping("/loginPage")
    public String loginPage(@RequestParam("url") String url, ModelMap modelMap) {
        if (checkUrl(url) && !"testurl".equals(url)) {
            modelMap.put("redirectUrl", url);
            return "index";
        }
        return "403";
    }


    /**
     * @Author wanggl(lane)
     * @Description //TODO 没有实际作用,在网关中已经处理
     * @Date 10:05 2019-03-07
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping("/check")
    public String check() {
        return "403";
    }

    /**
     * @Author wanggl(lane)
     * @Description //TODO 没有实际作用,在网关中已经处理
     * @Date 10:06 2019-03-07
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping("/logout")
    public String logout() {
        return "403";
    }

    private boolean checkUrl(String url) {
        if (StringUtils.isNotEmpty(url)) {
            System.out.println("111");
            return true;
        }
        return false;
    }

    private String generateToken(String username, String password) {
        String temp = username + System.currentTimeMillis() + password;
        return MD5(temp);
    }
}
