package com.dyny.userservice.service.impl;

import com.alibaba.nacos.client.config.utils.MD5;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyny.userservice.db.dao.UserMapper;
import com.dyny.userservice.db.entity.User;
import com.dyny.userservice.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 * @author wanggl
 * @since 2019-03-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public boolean changePassword(String oldPassword, String newPassword, int userId) {
        User user = getById(userId);
        String oldPasswordMd5 = MD5.getInstance().getMD5String(oldPassword);
        if (user.getPassword().equals(oldPasswordMd5)) {
            String newPasswordMd5 = MD5.getInstance().getMD5String(newPassword);
            user.setPassword(newPasswordMd5);
            return updateById(user);
        } else {
            return false;
        }
    }
}
