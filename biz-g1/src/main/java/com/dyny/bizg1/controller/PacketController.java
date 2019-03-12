package com.dyny.bizg1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-03-12 10:41
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/packet")
public class PacketController {

    @RequestMapping("/receive")
    public String receivedPacket(@RequestParam("data") String data, @RequestParam("cmd") String cmd) {

        return null;
    }
}
