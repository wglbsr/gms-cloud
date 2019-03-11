package com.dyny.userservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dyny.common.utils.BaseController;
import com.dyny.common.utils.BaseControllerT;
import com.dyny.userservice.db.entity.RelRoleAuth;
import com.dyny.userservice.service.RelRoleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-03-06
 */
@RestController
@RequestMapping(value = "/relRoleAuth", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class RelRoleAuthController extends BaseControllerT<RelRoleAuth> {
    @Autowired
    private RelRoleAuthService relRoleAuthService;


    @RequestMapping("/relate")
    public String relate(int roleId, int authId) {
        if (roleId > 0 && authId > 0) {
            RelRoleAuth relRoleAuth = new RelRoleAuth();
            relRoleAuth.setAuthId(authId);
            relRoleAuth.setRoleId(roleId);
            relRoleAuth.setCreateTime(LocalDateTime.now());
            return super.getSuccessResult(relRoleAuthService.save(relRoleAuth));
        } else {
            return super.getErrorMsg("角色id或权限id错误！");
        }

    }

    @RequestMapping("/unRelate")
    public String unRelate(int roleId, int authId) {
        QueryWrapper<RelRoleAuth> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("role_id", roleId).eq("auth_id", authId);
        return super.getSuccessResult(relRoleAuthService.remove(roleQueryWrapper));
    }
}
