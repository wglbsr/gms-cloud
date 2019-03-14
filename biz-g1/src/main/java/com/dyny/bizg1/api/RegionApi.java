package com.dyny.bizg1.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: lane
 * @Date: 2019-03-14 09:36
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("mid-region")
public interface RegionApi {

    @RequestMapping("/select/{id}")
    String select(@PathVariable("id") int id);


    @RequestMapping("/select")
    String getByParentId(@RequestParam("parentId") int parentId,
                         @RequestParam(value = "level", required = false, defaultValue = "0") int level);
}
