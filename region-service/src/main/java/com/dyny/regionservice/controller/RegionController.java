package com.dyny.regionservice.controller;

import com.dyny.utils.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-22 11:19
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/region-service")
public class RegionController extends BaseController {
    @RequestMapping("/test")
    public String get() {
        return "region-service";
    }
}
