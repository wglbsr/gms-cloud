package com.dyny.gateway.service;

/**
 * @Auther: lane
 * @Date: 2019-02-28 09:06
 * @Description:
 * @Version 1.0.0
 */
public interface AuthService {
    boolean updateToken(String token);

    String generateToken(String username, String password, int timeoutMin);
}
