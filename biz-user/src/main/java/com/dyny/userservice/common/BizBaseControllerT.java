package com.dyny.userservice.common;

import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.BaseControllerT;
import com.dyny.userservice.api.RedisApi;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: lane
 * @Date: 2019-03-07 09:21
 * @Description:
 * @Version 1.0.0
 */
public class BizBaseControllerT<T> extends BaseControllerT<T> {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected RedisApi redisApi;

    protected <UserT> UserT getUser(Class<UserT> UserT) {
        String userInfoStr = redisApi.get(getToken());
        UserT userT = JSONObject.parseObject(userInfoStr, UserT);
        return userT;
    }


    protected String getToken() {
        String token = request.getHeader(KEY_TOKEN);
        return token;
    }

    protected String getIp() {
        return request.getRemoteAddr();
    }
}
