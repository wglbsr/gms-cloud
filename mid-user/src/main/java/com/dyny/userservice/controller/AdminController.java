package com.dyny.userservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyny.common.controller.BaseController;
import com.dyny.common.controller.BaseControllerT;
import com.dyny.userservice.db.entity.User;
import com.dyny.userservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2018/11/17 15:41
 * @Description:
 * @Version 1.0.0
 */
@RequestMapping(value = "/admin", produces = {BaseController.ENCODE_CHARSET_UTF8})
@RestController
public class AdminController extends BaseControllerT<User> {
    @Autowired
    private UserService userService;


    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @RequestMapping("/addUser")
    public String addUser(@RequestBody User user) {
        return super.getSuccessResult(userService.save(user));
    }


    /**
     * 获取所有用户
     *
     * @return
     */
    @RequestMapping("/allUser")
    public String getAllUser(@RequestParam(name = KEY_PAGE_NUM, required = false, defaultValue = "1") long pageNum,
                             @RequestParam(name = KEY_KEY_WORD, required = false, defaultValue = "") String keyWord,
                             @RequestParam(name = KEY_PAGE_SIZE, required = false, defaultValue = "0") long pageSize) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(keyWord)) {
            userQueryWrapper.like("username", keyWord).or().like("phone", keyWord)
                    .or().like("nickname", keyWord).or().like("nickName", keyWord);
        }
        if (pageSize > 0) {
            return super.getSuccessPage(userService.page(new Page(pageNum, pageSize), userQueryWrapper));
        } else {
            return super.getSuccessResult(userService.list(userQueryWrapper));
        }
    }
}
