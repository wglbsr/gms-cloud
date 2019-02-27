package com.dyny.gateway.config;

import com.alibaba.fastjson.JSONObject;
import com.dyny.utils.BaseController;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: lane
 * @Date: 2019-02-26 16:49
 * @Description:
 * @Version 1.0.0
 */
public class ApiFilter extends ZuulFilter {

    private static Logger logger = LoggerFactory.getLogger(ApiFilter.class);

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
        String token = request.getHeader("TOKEN");
        String loginUrl = "/service-user/user/login";
        String loginPageUrl = "/service-user/user/sso";
        logger.info("uri:" + uri);
        //登录操作则直接放行
        if (loginUrl.equals(uri) || loginPageUrl.equals(uri)) {
            return null;
        }
        //        校验token
        if (StringUtils.isEmpty(token)) {
            logger.info("token为空，禁止访问!");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(BaseController.DATA_KEY, loginPageUrl);
            jsonObject.put(BaseController.RESULT_KEY, false);
            jsonObject.put(BaseController.ERROR_MSG_KEY, "need login!");
            jsonObject.put(BaseController.STATUS_KEY, 401);
            requestContext.getResponse().setContentType("text/html;charset=UTF-8");
            requestContext.setResponseBody(jsonObject.toJSONString());
            return null;
        }

        return null;
    }

}
