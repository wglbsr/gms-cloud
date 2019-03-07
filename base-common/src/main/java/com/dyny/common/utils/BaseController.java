package com.dyny.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dyny.common.enums.ErrorMsgEnum;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author wanggl
 * @Description 通用方法
 * @create 10:55 2018-8-15
 * @modify 15:2 2019-1-07
 **/
public class BaseController {

    public static final String URL_LOGIN = "/service-user/sso/login";
    public static final String URL_LOGIN_PAGE = "/service-user/sso/loginPage";
    public static final String URL_LOGOUT = "/service-user/sso/logout";
    public static final String URL_TOKEN_CHECK = "/service-user/sso/check";

    protected JSONObject result;
    public static final String KEY_EXPIRE_TIME = "expireTime";
    public static final String KEY_REDIRECT_URI = "redirectUrl";
    public static final String KEY_USER_INFO = "userInfo";
    public static final String KEY_DATA = "data";
    public static final String KEY_KEY_WORD = "keyWord";
    public static final String KEY_PAGE_SIZE = "pageSize";
    public static final String KEY_PAGE_NUM = "pageNum";
    public static final String KEY_TOTAL_PAGE_NUM = "totalPageNum";
    public static final String KEY_TOTAL_NUM = "totalNum";
    public static final String KEY_ERROR_MSG = "errorMsg";
    public static final String KEY_STATUS = "status";
    public static final String KEY_RESULT = "result";
    public static final String KEY_TOKEN = "auth_token";
    public static final String DEFAULT_ERROR_MSG = "内部错误!";
    public static final int DEFAULT_SUCCESS_STATUS = 200;
    public static final int NEED_LOGIN = 401;
    public static final int TIME_OUT_TOKEN_MIN = 60 * 24;

    /**
     * 返回成功信息
     *
     * @param data
     * @return
     */
    public String getSuccessResult(Object data) {
        long pageNum = 1;
        long pageSize = 0;
        long totalNum = 0;
        if (data instanceof List) {
            pageSize = ((List) data).size();
            totalNum = pageSize;
        }
        return this.getResult(true, data, "", pageNum, pageSize, totalNum);
    }

    public String getJsonStr(Object data) {
        return JSONObject.toJSONString(data, SerializerFeature.WriteMapNullValue);
    }


    public String getSuccessResult(Object data, long pageNum, long pageSize, long total) {
        return this.getResult(true, data, "", pageNum, pageSize, total);
    }


    /**
     * 返回错误信息
     *
     * @param data
     * @param errorMessage
     * @return
     */
    public String getErrorResult(Object data, String errorMessage) {
        return this.getResult(false, data, errorMessage, 0, 0, 0);
    }

    public String getErrorResult(Object data) {
        return this.getResult(false, data, "", 0, 0, 0);
    }

    public String getErrorMsg(String msg) {
        return this.getResult(false, null, msg, 0, 0, 0);
    }

    public String getErrorMsg(String msg, int status) {
        return this.getResult(false, null, msg, status, 0, 0, 0);
    }

    public String getErrorMsg(ErrorMsgEnum errorMsgEnum) {
        return this.getResult(false, null, errorMsgEnum.getName(), errorMsgEnum.getCode(), 0, 0, 0);
    }

    public String getError() {
        return this.getResult(false, null, null, 0, 0, 0);
    }


    private String getResult(boolean successFlag, Object data, String errorMessage, long pageNum, long pageSize, long totalNum) {
        return this.getResult(successFlag, data, errorMessage, DEFAULT_SUCCESS_STATUS, pageNum, pageSize, totalNum);
    }

    private String getResult(boolean successFlag, Object data, String errorMessage, int status, long pageNum, long pageSize, long totalNum) {
        this.result = new JSONObject();
        if (successFlag) {
            this.result.put(KEY_STATUS, status);
            this.result.put(KEY_DATA, data);
            this.result.put(KEY_PAGE_SIZE, pageSize);
            this.result.put(KEY_PAGE_NUM, pageNum);
            long totalPageNum = pageSize > 0 ? (totalNum / pageSize + (totalNum % pageSize > 0 ? 1 : 0)) : 0;
            this.result.put(KEY_TOTAL_PAGE_NUM, totalPageNum);
            this.result.put(KEY_TOTAL_NUM, totalNum);
        } else {
            this.result.put(KEY_STATUS, status);
            this.result.put(KEY_DATA, null);
            this.result.put(KEY_PAGE_SIZE, pageSize);
            this.result.put(KEY_PAGE_NUM, pageNum);
            this.result.put(KEY_ERROR_MSG, StringUtils.isEmpty(errorMessage) ? DEFAULT_ERROR_MSG : errorMessage);
        }
        this.result.put(KEY_RESULT, successFlag);
        return JSONObject.toJSONString(this.result, SerializerFeature.WriteMapNullValue);
    }



    public String getLoginResult(String token, Object user) {
        this.result = new JSONObject();
        this.result.put(KEY_TOKEN, token);
        this.result.put("userInfo", user);
        this.result.put(KEY_RESULT, true);
        return JSONObject.toJSONString(this.result, SerializerFeature.WriteMapNullValue);
    }

    public JSONObject getJsonData(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        if (jsonObject == null) {
            return null;
        }
        Boolean result = jsonObject.getObject(KEY_RESULT, Boolean.class);
        if (!result) {
            return null;
        }
        return jsonObject.getJSONObject(KEY_DATA);
    }

    public String getStrData(String jsonStr) {
        return getJsonData(jsonStr).toJSONString();
    }

    public <T> T getData(String jsonStr, Class<T> t) {
        String data = getStrData(jsonStr);
        if (!StringUtils.isEmpty(data)) {
            return JSONObject.parseObject(data, t);
        } else {
            return null;
        }
    }

    /**
     * md5加密，多用于密码
     *
     * @param content
     * @return
     */
    public String MD5(String content) {
        if (!StringUtils.isEmpty(content)) {
            return DigestUtils.md5DigestAsHex(content.getBytes());
        } else {
            return "";
        }
    }

    public ResponseEntity<byte[]> getFile(String path, String fileName) throws IOException {
        return getFile(path + "/" + fileName);
    }

    public ResponseEntity<byte[]> getFile(String fullPath) throws IOException {
        File file = new File(fullPath);
        if (!file.exists()) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }



}
