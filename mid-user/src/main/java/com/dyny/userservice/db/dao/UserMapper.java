package com.dyny.userservice.db.dao;

import com.dyny.userservice.db.entity.User;
import com.dyny.common.db.CommonMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wanggl
 * @since 2019-03-05
 */
@Mapper
public interface UserMapper extends CommonMapper<User> {

}
