package com.dyny.userservice.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: lane
 * @Date: 2019-02-22 11:30
 * @Description:
 * @Version 1.0.0
 */
@FeignClient(name="mid-region")
public interface RegionApi {

    @RequestMapping("/region/test")
    String testRegion();
}
