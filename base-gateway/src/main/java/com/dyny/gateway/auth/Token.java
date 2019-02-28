package com.dyny.gateway.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * @Auther: lane
 * @Date: 2019-02-28 11:22
 * @Description:
 * @Version 1.0.0
 */
public class Token {
    private String ip;
    private String token;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    //超时,分钟
    private int timeout = 60 * 3;

    public Token(String username, String password) {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.expireTime = now.plusMinutes(this.timeout);
        String timestampStr = Timestamp.valueOf(now).getTime() + "";
        this.token = Base64.getEncoder().encodeToString((username + password + timestampStr).getBytes());
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public boolean update() {
        if (!this.expireTime.isBefore(LocalDateTime.now())) {
            this.expireTime = this.expireTime.plusMinutes(this.timeout);
            return true;
        } else {
            return false;
        }
    }
}
