package com.dyny.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyny.userservice.db.dao.RelRoleAuthMapper;
import com.dyny.userservice.db.entity.RelRoleAuth;
import com.dyny.userservice.service.RelRoleAuthService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-05
 */
@Service
public class RelRoleAuthServiceImpl extends ServiceImpl<RelRoleAuthMapper, RelRoleAuth> implements RelRoleAuthService {

    @Override
    public List<RelRoleAuth> selectByRoleIdList(List<Integer> roleIdList) {
        QueryWrapper<RelRoleAuth> relRoleAuthQueryWrapper = new QueryWrapper<>();
        relRoleAuthQueryWrapper.in("role_id", roleIdList);
        return list(relRoleAuthQueryWrapper);
    }
}
