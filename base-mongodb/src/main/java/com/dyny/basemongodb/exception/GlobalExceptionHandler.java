package com.dyny.basemongodb.exception;

import com.dyny.common.controller.BaseController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @project: gms
 * @description: 全局异常记录
 * @author: wanggl
 * @create: 2018-11-02 09:19
 **/
@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String errorRecorder(Exception e) {
        e.printStackTrace();
        String errorMsg = e.getMessage();
        return super.getErrorMsg(errorMsg);
    }
}
