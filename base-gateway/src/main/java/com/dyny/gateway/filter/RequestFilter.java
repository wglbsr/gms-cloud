package com.dyny.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.BaseController;
import com.dyny.gateway.api.RedisApi;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: lane
 * @Date: 2019-02-26 16:49
 * @Description:
 * @Version 1.0.0
 */
@Component
public class RequestFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();
        String token = request.getHeader(BaseController.KEY_TOKEN);
        String loginUrl = "/service-user/sso/login";
        String loginIndex = "/service-user/sso/index";
        logger.info("uri:" + uri);
        //登录操作则直接放行
        if (loginUrl.equals(uri) || loginIndex.equals(uri)) {
            return null;
        }
        //校验token
        if (tokenCheck(token)) {
            requestContext.setSendZuulResponse(true);
            requestContext.setResponseStatusCode(200);
        } else {
            logger.info("token不合法，禁止访问!");
            BaseController baseController = new BaseController();
            requestContext.setResponseBody(baseController.getErrorMsg("need login!", BaseController.NEED_LOGIN));
        }
        return null;
    }

    @Autowired
    RedisApi redisApi;

    private boolean tokenCheck(String token) {

        String result = redisApi.get(token);
        JSONObject.parseObject(result);

        //验证token
        logger.info("redis cache!!!!!!!!!" + redisApi.get(token));
        //更新token时间

        return false;
    }


}
