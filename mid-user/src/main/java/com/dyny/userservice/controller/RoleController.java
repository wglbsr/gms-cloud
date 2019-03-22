package com.dyny.userservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyny.common.controller.BaseController;
import com.dyny.userservice.common.BizBaseControllerT;
import com.dyny.userservice.db.entity.Role;
import com.dyny.userservice.db.entity.User;
import com.dyny.userservice.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/role", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class RoleController extends BizBaseControllerT<Role> {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/create")
    public String createRole(@RequestBody Role role) {
        role.setCreateTime(LocalDateTime.now());
        role.setActivated(true);
        return super.getSuccessResult(roleService.save(role));
    }

    @RequestMapping("/update")
    public String updateRole(@RequestBody Role role) {
        return super.getSuccessResult(roleService.saveOrUpdate(role));
    }

    @RequestMapping("/activate")
    public String activate(boolean activate, int id) {
        Role role = roleService.getById(id);
        role.setActivated(activate);
        return super.getSuccessResult(roleService.saveOrUpdate(role));
    }

    @RequestMapping("/delete")
    public String deleteRole(@RequestParam(name = "roleId") int roleId) {
        return super.getSuccessResult(roleService.removeById(roleId));
    }

    @RequestMapping("/select")
    public String selectRole(@RequestParam(name = KEY_KEY_WORD, required = false, defaultValue = "") String keyWord,
                             @RequestParam(name = KEY_PAGE_NUM, required = false, defaultValue = "1") long pageNum,
                             @RequestParam(name = KEY_PAGE_SIZE, required = false, defaultValue = "0") long pageSize) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(keyWord)) {
            roleQueryWrapper.like("role_name", keyWord).or().like("description", keyWord);
        }
        return super.getSuccessPage(roleService.page(new Page(pageNum, pageSize), roleQueryWrapper));
    }

    @RequestMapping("/selectByUserId")
    public String selectAuth(@RequestParam(name = "userId") int userId) {
        return super.getSuccessResult(roleService.selectRoleByUserId(userId));
    }


    @RequestMapping("/getMyRole")
    public String getMyRole() {
        User user = getUser(User.class);
        return super.getSuccessResult(roleService.selectRoleByUserId(user.getId()));
    }
}

