package com.dyny.userservice.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: lane
 * @Date: 2019-03-11 11:42
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("base-mongodb")
public interface MongodbApi {

    @RequestMapping("/file/delete")
    String deleteFile(@RequestParam("fileId") String fileId);
}
