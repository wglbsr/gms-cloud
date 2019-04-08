package com.dyny.common.controller;

/**
 * @Auther: lane
 * @Date: 2019-03-07 09:21
 * @Description: 该类用于需要使用用户信息的模块, 需要直接拷贝到模块当中再继承, 不能直接继承, 因为涉及自动注入, 拷贝后去掉注释即可继承使用,也可以自己另外写
 * @Version 1.0.0
 */
public class BizBaseController extends BaseController {
    //    @Autowired
//    protected HttpServletRequest request;
//    @Autowired
//    protected RedisApi redisApi;
//
//    protected <UserT> UserT getUser(Class<UserT> UserT) {
//        String userInfoStr = redisApi.get(getToken());
//        UserT userT = JSONObject.parseObject(userInfoStr, UserT);
//        return userT;
//    }
//
//
//    protected String getToken() {
//        String token = request.getHeader(KEY_TOKEN);
//        return token;
//    }
//
//    protected String getIp() {
//        return request.getRemoteAddr();
//    }
//    protected Integer getUserId() {
//        String userInfoStr = redisApi.get(getToken());
//        JSONObject jsonObject = JSONObject.parseObject(userInfoStr);
//        return (Integer) jsonObject.get("id");
//    }
}
