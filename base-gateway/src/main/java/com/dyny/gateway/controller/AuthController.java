package com.dyny.gateway.controller;

import com.dyny.common.utils.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-28 11:27
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
//    @Autowired
//    UserApi userApi;
//
//    @RequestMapping("/login")
//    public String getToken(@RequestParam("username") String username,
//                           @RequestParam("password") String password,
//                           @RequestParam("url") String url) {
//        String result = userApi.login(username, password);
//        JSONObject userInfo = getJsonData(userApi.login(username, password));
//        if (userInfo != null) {
//            Token token = new Token(username, password);
//            JSONObject newData = new JSONObject();
//            newData.put(BaseController.KEY_TOKEN, token.getToken());
//            newData.put(BaseController.KEY_USER_INFO, userInfo);
//            newData.put(BaseController.KEY_EXPIRE_TIME, token.getExpireTime());
//            newData.put(BaseController.KEY_REDIRECT_URI, url);
//            return getSuccessResult(newData);
//        } else {
//            return result;
//        }
//    }

}
