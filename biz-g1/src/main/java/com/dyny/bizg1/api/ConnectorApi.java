package com.dyny.bizg1.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: lane
 * @Date: 2019-03-12 10:44
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("mid-connector-v2")
public interface ConnectorApi {

    @RequestMapping("/cmd/send/{systemId}/{deviceId}")
    String sendCmd(@PathVariable("systemId") int systemId,
                   @PathVariable("deviceId") int deviceId,
                   @RequestParam("cmd") String cmd,
                   @RequestParam("data") String data,
                   @RequestParam(value = "connectType", required = false, defaultValue = "1") int connectType);
}
