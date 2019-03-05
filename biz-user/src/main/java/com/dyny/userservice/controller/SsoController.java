package com.dyny.userservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.BaseController;
import com.dyny.userservice.api.RedisApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.Base64Utils;
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
@RequestMapping("/sso")
@RefreshScope
public class SsoController extends BaseController {

    private String loginService(String username, String password) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("age", 18);
        jsonObject.put("gender", 1);
        return getSuccessResult(jsonObject);
    }

    @Autowired
    RedisApi redisApi;
    @Value("${tokenTimeoutMin:1440}")
    private int tokenTimeoutMin;

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        String resultStr = loginService(username, password);
        JSONObject resultObject = JSONObject.parseObject(resultStr);
        JSONObject userInfo = resultObject.getJSONObject(BaseController.KEY_DATA);
        if (userInfo != null) {
            String token = generateToken(username);
            resultObject.put(BaseController.KEY_TOKEN, token);
            redisApi.set(token, userInfo.toJSONString(), tokenTimeoutMin);
        }
        return resultObject.toJSONString();
    }


    @RequestMapping("/loginPage")
    public String loginPage(@RequestParam("url") String url) {
        return "index";
    }

    private String generateToken(String username) {
        String temp = username + System.currentTimeMillis();
        return Base64Utils.encodeToString(temp.getBytes());
    }
}
