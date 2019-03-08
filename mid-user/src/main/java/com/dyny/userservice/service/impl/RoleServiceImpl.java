package com.dyny.userservice.service.impl;

import com.dyny.userservice.db.entity.Role;
import com.dyny.userservice.db.dao.RoleMapper;
import com.dyny.userservice.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> selectRoleByUserId(int userId) {
        return baseMapper.selectRoleByUserId(userId);
    }
}
