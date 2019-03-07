package com.dyny.userservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.BaseController;
import com.dyny.userservice.api.RedisApi;
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
@RequestMapping("/sso")
@Controller
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
    @Value("${tokenTimeoutMin:" + TIME_OUT_TOKEN_MIN + "}")
    private int tokenTimeoutMin;

    @RequestMapping("/login")
    @ResponseBody
    public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
        String resultStr = loginService(username, password);
        JSONObject resultObject = JSONObject.parseObject(resultStr);
        JSONObject userInfo = resultObject.getJSONObject(BaseController.KEY_DATA);
        if (userInfo != null) {
            String token = generateToken(username, password);
            resultObject.put(BaseController.KEY_TOKEN, token);
            redisApi.set(token, userInfo.toJSONString(), tokenTimeoutMin);
        }
        return resultObject.toJSONString();
    }


    @RequestMapping("/loginPage")
    public String loginPage(@RequestParam("url") String url, ModelMap modelMap) {
        if (checkUrl(url) && !"testurl".equals(url)) {
            modelMap.put("redirectUrl", url);
            return "index";
        }
        return "403";
    }


    @RequestMapping("/check")
    public String check() {
        return "403";
    }

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
