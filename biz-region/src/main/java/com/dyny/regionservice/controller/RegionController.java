package com.dyny.regionservice.controller;

import com.dyny.common.utils.BaseController;
import com.dyny.common.utils.Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-22 11:19
 * @Description:
 * @Version 1.0.0
 */
@RequestMapping("/region")
@RestController
public class RegionController extends BaseController {
    @RequestMapping("/test")
    public String get() {
        if (Utils.String.isNum("123123")) {

        }

        return "service-region-";
    }
}
