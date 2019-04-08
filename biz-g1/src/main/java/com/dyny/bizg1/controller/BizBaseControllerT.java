package com.dyny.bizg1.controller;

import com.alibaba.fastjson.JSONObject;
import com.dyny.bizg1.api.RedisApi;
import com.dyny.common.controller.BaseControllerT;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: lane
 * @Date: 2019-03-07 09:21
 * @Description: 该类用于需要使用用户信息的模块, 需要直接拷贝到模块当中再继承, 不能直接继承, 因为涉及自动注入, 拷贝后去掉注释即可继承使用
 * @Version 1.0.0
 */
public class BizBaseControllerT<T> extends BaseControllerT<T> {
    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected RedisApi redisApi;

    protected <UserT> UserT getUser(Class<UserT> UserT) {
        String userInfoStr = redisApi.get(getToken());
        UserT userT = JSONObject.parseObject(userInfoStr, UserT);
        return userT;
    }

    protected Integer getUserId() {
        String userInfoStr = redisApi.get(getToken());
        JSONObject data = getJsonData(userInfoStr);
        return (Integer) data.get("id");
    }


    protected String getToken() {
        String token = request.getHeader(KEY_TOKEN);
        return token;
    }

    protected String getIp() {
        return request.getRemoteAddr();
    }
}
