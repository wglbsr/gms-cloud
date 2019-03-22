package com.dyny.bizg1.controller;


import com.dyny.common.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@RestController
@RequestMapping(value = "/relStationCarrier", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class RelStationCarrierController extends BaseController {

}

