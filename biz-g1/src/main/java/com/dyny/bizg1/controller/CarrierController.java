package com.dyny.bizg1.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.dyny.common.utils.BaseController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@RestController
@RequestMapping(value = "/carrier", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class CarrierController extends BaseController {

}

