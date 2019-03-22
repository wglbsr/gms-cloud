package com.dyny.userservice.db.dao;

import com.dyny.userservice.db.entity.Auth;
import com.dyny.common.db.CommonMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wanggl
 * @since 2019-03-05
 */
@Mapper
public interface AuthMapper extends CommonMapper<Auth> {
    @Select("select * from sys_auth where id in(select auth_id from rel_role_auth where role_id in(select role_id from rel_user_auth where user_id = #{userId}))")
    List<Auth> selectByUserId(@Param("userId") int userId);
}
