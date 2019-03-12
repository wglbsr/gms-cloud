package com.dyny.userservice.controller;

import com.dyny.common.utils.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-03-12 16:17
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/system", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class SystemController extends BaseController {

    @RequestMapping("/select")
    public String select() {
        return getSuccessResult();
    }
}
