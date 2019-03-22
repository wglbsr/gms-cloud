package com.dyny.bizg1.controller;


import com.dyny.bizg1.service.CustomerService;
import com.dyny.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/selectAll")
    public String selectAll() {
        return getSuccessResult(customerService.list());
    }
}

