package com.dyny.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dyny.enums.ErrorMsgEnum;
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
    protected JSONObject result;
    public static final String DATA_KEY = "data";
    public static final String PAGE_SIZE_KEY = "pageSize";
    public static final String PAGE_NUM_KEY = "pageNum";
    public static final String TOTAL_PAGE_NUM_KEY = "totalPageNum";
    public static final String TOTAL_NUM_KEY = "totalNum";
    public static final String ERROR_MSG_KEY = "errorMsg";
    public static final String STATUS_KEY = "status";
    public static final String RESULT_KEY = "result";
    public static final String TOKEN_KEY = "token";
    public static final String DEFAULT_ERROR_MSG = "内部错误!";
    public static final int DEFAULT_SUCCESS_STATUS = 200;
    public static final int NEED_LOGIN = 401;

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
            this.result.put(STATUS_KEY, status);
            this.result.put(DATA_KEY, data);
            this.result.put(PAGE_SIZE_KEY, pageSize);
            this.result.put(PAGE_NUM_KEY, pageNum);
            long totalPageNum = pageSize > 0 ? (totalNum / pageSize + (totalNum % pageSize > 0 ? 1 : 0)) : 0;
            this.result.put(TOTAL_PAGE_NUM_KEY, totalPageNum);
            this.result.put(TOTAL_NUM_KEY, totalNum);
        } else {
            this.result.put(DATA_KEY, null);
            this.result.put(PAGE_SIZE_KEY, pageSize);
            this.result.put(PAGE_NUM_KEY, pageNum);
            this.result.put(ERROR_MSG_KEY, StringUtils.isEmpty(errorMessage) ? DEFAULT_ERROR_MSG : errorMessage);
        }
        this.result.put(RESULT_KEY, successFlag);
        return JSONObject.toJSONString(this.result, SerializerFeature.WriteMapNullValue);
    }

    public String getToken(String token) {
        this.result = new JSONObject();
        this.result.put("token", token);
        return JSONObject.toJSONString(this.result, SerializerFeature.WriteMapNullValue);
    }


    public String tokenAndUser(String token, Object user) {
        this.result = new JSONObject();
        this.result.put(TOKEN_KEY, token);
        this.result.put("userInfo", user);
        this.result.put(RESULT_KEY, true);
        return JSONObject.toJSONString(this.result, SerializerFeature.WriteMapNullValue);
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
