package com.dyny.userservice.controller;


import com.alibaba.fastjson.JSONObject;
import com.dyny.userservice.common.BizBaseControllerT;
import com.dyny.userservice.db.entity.User;
import com.dyny.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-03-06
 */
@RestController
@RefreshScope
@RequestMapping(value = UserController.USER_URI, produces = {"application/json;charset=UTF-8"})
public class UserController extends BizBaseControllerT<User> {
    public final static String USER_URI = "/user";
    @Autowired
    private UserService userService;
    @Value("${tokenTimeoutMin:" + TIME_OUT_TOKEN_MIN + "}")
    private int tokenTimeoutMin;

    /**
     * 信息修改
     *
     * @param user
     * @return
     */
    @RequestMapping("/changeInfo")
    public String changeInfo(@RequestBody User user) {
        boolean result = userService.saveOrUpdate(user);
        if (result) {
            user.setPassword(null);
            redisApi.set(getToken(), JSONObject.toJSONString(user), tokenTimeoutMin);
        }
        return super.getSuccessResult(result);
    }


    @RequestMapping("/userInfo")
    public String getUserInfo() {
        User user = getUser(User.class);
        return super.getSuccessResult(userService.getById(user.getId()));
    }

    //1.密码修改
    @RequestMapping("/changePsw")
    public String changePsw(
            @RequestParam(name = "oldPassword") String oldPassword,
            @RequestParam(name = "newPassword") String newPassword) {
        return getSuccessResult(userService.changePassword(oldPassword, newPassword, getUser(User.class).getId()));

    }
}

