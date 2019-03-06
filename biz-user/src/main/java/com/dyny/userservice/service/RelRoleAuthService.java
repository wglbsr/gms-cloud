package com.dyny.userservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dyny.userservice.db.entity.RelRoleAuth;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-06
 */
public interface RelRoleAuthService extends IService<RelRoleAuth> {
    List<RelRoleAuth> selectByRoleIdList(List<Integer> roleIdList);
}
