package com.dyny.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyny.userservice.db.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-06
 */
public interface RoleService extends IService<Role> {
    List<Role> selectRoleByUserId(int userId);
}
