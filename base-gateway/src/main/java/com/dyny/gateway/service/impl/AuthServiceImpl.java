package com.dyny.gateway.service.impl;

import com.dyny.gateway.service.AuthService;

/**
 * @Auther: lane
 * @Date: 2019-02-28 09:17
 * @Description:
 * @Version 1.0.0
 */
public class AuthServiceImpl implements AuthService {
    @Override
    public boolean updateToken(String token) {
        return false;
    }


    @Override
    public String generateToken(String username, String password, int timeoutMin) {
        return null;
    }

}
