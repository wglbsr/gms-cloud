package com.dyny.userservice.controller;


import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.BaseController;
import com.dyny.userservice.api.MongodbApi;
import com.dyny.userservice.common.BizBaseControllerT;
import com.dyny.userservice.db.entity.User;
import com.dyny.userservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

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
@RequestMapping(value = UserController.USER_URI, produces = {BaseController.ENCODE_CHARSET_UTF8})
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
            return super.getSuccessResult(user);
        } else {
            return getErrorMsg("修改失败");
        }
    }

    @RequestMapping("/create")
    public String create(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("level") int level) {
        User user = new User();
        user.setPassword(MD5(password));
        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setModifyTime(now);
        user.setLocked(false);
        user.setNickname(username);
        user.setUsername(username);
        user.setLevel(level);
        return super.getSuccessResult(userService.save(user) ? user.getId() : null);
    }


    @RequestMapping("/userInfo")
    public String getUserInfo() {
        User user = getUser(User.class);
        return super.getSuccessResult(userService.getById(user.getId()));
    }

    @Autowired
    private MongodbApi mongodbApi;

    @RequestMapping("/changeAvatar")
    public String changeAvatar(@RequestParam("fileId") String fileId) {
        User user = getUser(User.class);
        user = userService.getById(user.getId());
        String oriAvatar = user.getAvatar();
        String oriFileId = null;
        if (StringUtils.isNotEmpty(oriAvatar)) {
            oriFileId = oriAvatar.substring(oriAvatar.lastIndexOf("/") + 1);
        }
        String newAvatarUrl = URL_FILE_DOWNLOAD + "/" + fileId;
        user.setAvatar(newAvatarUrl);
        boolean result = userService.updateById(user);
        if (result) {
            user.setPassword(null);
            redisApi.set(getToken(), JSONObject.toJSONString(user), tokenTimeoutMin);
            if (StringUtils.isNotEmpty(oriFileId)) {
                mongodbApi.deleteFile(oriFileId);
            }
            return super.getSuccessResult(newAvatarUrl);
        } else {
            return super.getErrorMsg("操作失败!");
        }
    }

    //1.密码修改
    @RequestMapping("/changePsw")
    public String changePsw(
            @RequestParam(name = "oldPassword") String oldPassword,
            @RequestParam(name = "newPassword") String newPassword) {
        return getSuccessResult(userService.changePassword(oldPassword, newPassword, getUser(User.class).getId()));

    }
}

