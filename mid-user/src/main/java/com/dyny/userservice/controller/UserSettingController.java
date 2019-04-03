package com.dyny.userservice.controller;

import com.dyny.common.controller.BaseController;
import com.dyny.userservice.api.MongodbApi;
import com.dyny.userservice.db.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Auther: lane
 * @Date: 2019-04-03 16:30
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/" + UserSettingController.KEY_SETTING, produces = {BaseController.ENCODE_CHARSET_UTF8})
public class UserSettingController extends BizBaseController {
    static final String KEY_SETTING = "setting";
    private static final String SEPARATOR = "_";

    @Autowired
    private MongodbApi mongodbApi;

    @GetMapping("/{settingType}")
    public String getSetting(@PathVariable("settingType") String settingType) {
        User user = getUser(User.class);
        Integer userId = user.getId();
        if (StringUtils.isNotEmpty(settingType) && userId > 0) {
            return mongodbApi.select(getTableName(settingType), get12BytesUserId(userId));
        }
        return getErrorMsg("参数或路径错误!");
    }

    @PutMapping("/{settingType}")
    public String updateSetting(@PathVariable("settingType") String settingType, @RequestParam("setting") String setting) {
        User user = getUser(User.class);
        Integer userId = user.getId();
        if (StringUtils.isNotEmpty(settingType) && userId > 0) {
            return mongodbApi.update(getTableName(settingType), get12BytesUserId(userId), setting);
        }
        return getErrorMsg("参数或路径错误!");
    }

    @PostMapping("/{settingType}")
    public String createSetting(@PathVariable("settingType") String settingType, @RequestParam("setting") String setting) {
        User user = getUser(User.class);
        Integer userId = user.getId();
        if (StringUtils.isNotEmpty(settingType) && userId > 0) {
            return mongodbApi.insert(getTableName(settingType), get12BytesUserId(userId), setting);
        }
        return getErrorMsg("参数或路径错误!");
    }

    @DeleteMapping("/{settingType}")
    public String createSetting(@PathVariable("settingType") String settingType) {
        User user = getUser(User.class);
        Integer userId = user.getId();
        if (StringUtils.isNotEmpty(settingType) && userId > 0) {
            return mongodbApi.delete(getTableName(settingType), get12BytesUserId(userId));
        }
        return getErrorMsg("参数或路径错误!");
    }

    private String getTableName(String settingType) {
        return KEY_SETTING + SEPARATOR + settingType;
    }


}
