package com.dyny.bizg1.controller;


import com.dyny.bizg1.db.entity.GmsUser;
import com.dyny.bizg1.service.GmsUserService;
import com.dyny.common.controller.BaseController;
import com.dyny.common.controller.BaseControllerT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@RestController
@RequestMapping(value = "/gmsUser", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class GmsUserController extends BaseControllerT<GmsUser> {
    @Autowired
    private GmsUserService gmsUserService;

    @RequestMapping("/create")
    public String createUser(@RequestParam("userId") int userId,
                             @RequestParam("regionId") int regionId,
                             @RequestParam("customerId") int customerId) {
        GmsUser user = new GmsUser();
        user.setUserId(userId);
        user.setCustomerId(customerId);
        user.setCreateTime(LocalDateTime.now());
//        user.setRegionId(regionId);
        return getSuccessResult(gmsUserService.save(user));
    }


    @RequestMapping("/change")
    public String changeInfo(@RequestParam("userId") int userId,
                             @RequestParam("regionId") int regionId,
                             @RequestParam("customerId") int customerId) {
        GmsUser user = gmsUserService.getById(userId);
        user.setCustomerId(customerId);
//        user.setRegionId(regionId);
        return getSuccessResult(gmsUserService.updateById(user));
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam int userId) {
        return getSuccessResult(gmsUserService.removeById(userId));
    }


    @RequestMapping("/select")
    public String select(@RequestParam int userId) {
        return getSuccessResult(gmsUserService.getById(userId));
    }


}