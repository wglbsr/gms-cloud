package com.dyny.auth.service.impl;

import com.dyny.auth.service.BaseUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: lane
 * @Date: 2019-02-28 17:31
 * @Description:
 * @Version 1.0.0
 */
@Service
public class BaseUserDetailsServiceImpl implements BaseUserDetailsService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BaseUserService baseUserService;
    @Autowired
    private BaseRoleService baseRoleService;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        // 调用FeignClient查询用户
        ResponseData<BaseUser> baseUserResponseData = baseUserService.getUserByUserName(username);
        if(baseUserResponseData.getData() == null || !ResponseCode.SUCCESS.getCode().equals(baseUserResponseData.getCode())){
            logger.error("找不到该用户，用户名：" + username);
            throw new UsernameNotFoundException("找不到该用户，用户名：" + username);
        }
        BaseUser baseUser = baseUserResponseData.getData();

        // 调用FeignClient查询角色
        ResponseData<List<BaseRole>> baseRoleListResponseData = baseRoleService.getRoleByUserId(baseUser.getId());
        List<BaseRole> roles;
        if(baseRoleListResponseData.getData() == null ||  !ResponseCode.SUCCESS.getCode().equals(baseRoleListResponseData.getCode())){
            logger.error("查询角色失败！");
            roles = new ArrayList<>();
        }else {
            roles = baseRoleListResponseData.getData();
        }

        // 获取用户权限列表
        List<GrantedAuthority> authorities = new ArrayList();
        roles.forEach(e -> {
            // 存储用户、角色信息到GrantedAuthority，并放到GrantedAuthority列表
            GrantedAuthority authority = new SimpleGrantedAuthority(e.getRoleCode());
            authorities.add(authority);

        });

        // 返回带有用户权限信息的User
        org.springframework.security.core.userdetails.User user =  new org.springframework.security.core.userdetails.User(baseUser.getUserName(),
                baseUser.getPassword(), isActive(baseUser.getActive()), true, true, true, authorities);
        return new BaseUserDetail(baseUser, user);
    }

    private boolean isActive(int active){
        return active == 1 ? true : false;
    }
}
