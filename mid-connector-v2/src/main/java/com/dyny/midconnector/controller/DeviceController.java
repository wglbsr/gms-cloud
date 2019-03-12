package com.dyny.midconnector.controller;

import com.dyny.common.utils.BaseController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-03-08 15:51
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/device", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class DeviceController extends BaseController {
    /**
     * @return java.lang.String
     * @Author wanggl(lane)
     * @Description //TODO
     * @Date 08:51 2019-03-12
     * @Param [deviceId, cmd]
     **/
    @RequestMapping("/cmd/send/{systemId}/{deviceId}")
    public String sendCmd(@PathVariable("systemId") int systemId, @PathVariable("deviceId") int deviceId,
                          @RequestParam("cmd") String cmd, @RequestParam("data") String data, @RequestParam(value = "connectType", required = false, defaultValue = "1") int connectType) {



        return getSuccessResult(1);
    }
}
