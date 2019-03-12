package com.dyny.userservice.db.dao;

import com.dyny.userservice.db.entity.Role;
import com.dyny.common.utils.CommonMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wanggl
 * @since 2019-03-05
 */
@Mapper
public interface RoleMapper extends CommonMapper<Role> {
    @Select("select * from sys_role where id in (select role_id from sys_rel_user_role where user_id =#{userId})")
    List<Role> selectRoleByUserId(@Param("userId") int userId);
}
