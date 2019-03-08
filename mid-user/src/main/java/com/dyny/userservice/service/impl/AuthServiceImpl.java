package com.dyny.userservice.service.impl;

import com.dyny.userservice.db.entity.Auth;
import com.dyny.userservice.db.dao.AuthMapper;
import com.dyny.userservice.service.AuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-05
 */
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth> implements AuthService {

    @Override
    public List<Auth> selectByUserId(int userId) {
        return baseMapper.selectByUserId(userId);
    }
}
