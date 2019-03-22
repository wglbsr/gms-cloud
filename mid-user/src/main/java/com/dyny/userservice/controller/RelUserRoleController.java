package com.dyny.userservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dyny.common.controller.BaseController;
import com.dyny.common.controller.BaseControllerT;
import com.dyny.userservice.db.entity.RelUserRole;
import com.dyny.userservice.service.RelUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping(value = "/relUserRole", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class RelUserRoleController extends BaseControllerT<RelUserRole> {
    @Autowired
    private RelUserRoleService relUserRoleService;

    @RequestMapping("/relate")
    public String relate(int userId, int roleId) {
        RelUserRole relUserRole = new RelUserRole();
        relUserRole.setUserId(userId);
        relUserRole.setRoleId(roleId);
        boolean result = relUserRoleService.save(relUserRole);
        return getSuccessResult(result);
    }

    @RequestMapping("/unRelate")
    public String unRelate(int userId, int roleId) {
        QueryWrapper<RelUserRole> relUserRoleQueryWrapper = new QueryWrapper<>();
        relUserRoleQueryWrapper.eq("user_id", userId).eq("role_id", roleId);
        return getSuccessResult(relUserRoleService.remove(relUserRoleQueryWrapper));
    }

}
