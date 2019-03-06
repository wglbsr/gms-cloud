package com.dyny.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyny.userservice.db.entity.Auth;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-05
 */
public interface AuthService extends IService<Auth> {

    List<Auth> selectByUserId(int userId);
}
