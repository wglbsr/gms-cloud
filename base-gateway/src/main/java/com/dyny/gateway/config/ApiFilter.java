package com.dyny.gateway.config;

import com.dyny.gateway.api.UserApi;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserApi userApi;
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();
        String token = request.getHeader("TOKEN");
        //校验token
//        if (StringUtils.isEmpty(token)) {
//            logger.info("token为空，禁止访问!");
//            ctx.setSendZuulResponse(false);
//            ctx.setResponseStatusCode(401);
//            return null;
//        } else {


//        }

        //添加Basic Auth认证信息
        requestContext.addZuulRequestHeader("Authorization", "Basic " + getBase64Credentials("app01", "*****"));

        return null;
    }

    private String getBase64Credentials(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        return new String(base64CredsBytes);
    }
}
