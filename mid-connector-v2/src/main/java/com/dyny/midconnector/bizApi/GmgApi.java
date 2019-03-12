package com.dyny.midconnector.bizApi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: lane
 * @Date: 2019-03-12 10:09
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("biz-g1")
public interface GmgApi {
    @RequestMapping("/packet/receive")
    String receivedPacket(@RequestParam("data") String data, @RequestParam("cmd") String cmd);
}
