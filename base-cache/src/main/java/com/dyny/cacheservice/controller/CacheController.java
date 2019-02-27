package com.dyny.cacheservice.controller;

import com.dyny.utils.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-27 10:40
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/cache")
public class CacheController extends BaseController {

    @RequestMapping("/get")
    public String get(String key) {
        return getSuccessResult("{test:'123'}");
    }

    @RequestMapping("/set")
    public String set(String key,String value) {
        return getSuccessResult("1");
    }
}
