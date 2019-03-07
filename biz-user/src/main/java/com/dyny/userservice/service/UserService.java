package com.dyny.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyny.userservice.db.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-06
 */
public interface UserService extends IService<User> {
    boolean changePassword(String oldPassword, String newPassword, int userId);

    User login(String username, String password);
}
