package com.dyny.gdmodule.controller;


import com.dyny.common.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-08-13
 */
@RestController
@RequestMapping("/packet")
public class PacketController extends BaseController {
    @RequestMapping("/decode")
    public String decode(int type, int prop, int length, int prodSerialNum, int frameNum, byte[] payloadBytes0) {


        return null;
    }
}

