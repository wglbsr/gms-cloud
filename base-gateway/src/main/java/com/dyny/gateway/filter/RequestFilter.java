package com.dyny.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.BaseController;
import com.dyny.gateway.api.RedisApi;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
@RefreshScope
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
        logger.info("uri:" + uri);
        //登录或者文件上传操作则直接放行,这里存在安全隐患,上传文件应该需要判断手否有token
        if (BaseController.URL_LOGIN.equals(uri) || BaseController.URL_LOGIN_PAGE.equals(uri)) {
            requestContext.setSendZuulResponse(true);
            requestContext.setResponseStatusCode(200);
            return null;
        }
        String token;
        if (uri.contains(BaseController.URL_FILE_DOWNLOAD)) {//这里的下载链接判断不够严谨，可以使用先行零宽断言正则
            //这里应该直接从request中获取token,无法从头部获取token
            token = request.getParameter(BaseController.KEY_TOKEN);
        } else {
            token = request.getHeader(BaseController.KEY_TOKEN);
        }


        //校验token
        if (tokenCheck(token)) {
            //登出
            if (BaseController.URL_LOGOUT.equals(uri)) {
                this.logout(requestContext, token);
            } else if (BaseController.URL_TOKEN_CHECK.equals(uri)) {
                BaseController baseController = new BaseController();
                requestContext.setSendZuulResponse(false);
                requestContext.setResponseBody(baseController.getSuccessResult(1));
                requestContext.getResponse().setContentType(BaseController.ENCODE_CHARSET_UTF8);
            } else {
                requestContext.setSendZuulResponse(true);
                requestContext.setResponseStatusCode(200);
            }

        } else {
            logger.info("token不合法，禁止访问!");
            BaseController baseController = new BaseController();
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseBody(baseController.getErrorMsg("need login!", BaseController.NEED_LOGIN));
            requestContext.getResponse().setContentType(BaseController.ENCODE_CHARSET_UTF8);
        }
        return null;
    }


    @Autowired
    RedisApi redisApi;
    @Value("${tokenTimeoutMin:" + BaseController.TIME_OUT_TOKEN_MIN + "}")
    private int tokenTimeoutMin;

    private boolean tokenCheck(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        String result = redisApi.get(token);
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject != null) {
            JSONObject data = jsonObject.getJSONObject(BaseController.KEY_DATA);
            if (data != null) {
                redisApi.refresh(token, tokenTimeoutMin);
                return true;
            }
        }
        return false;
    }

    private void logout(RequestContext requestContext, String token) {
        redisApi.delete(token);
        BaseController baseController = new BaseController();
        requestContext.setResponseBody(baseController.getSuccessResult(1));
    }


}
