package com.dyny.redis.controller;

import com.dyny.common.utils.BaseController;
import com.dyny.redis.dao.CacheDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-02-27 16:02
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/redis", produces = {"application/json;charset=UTF-8"})
public class RedisController extends BaseController {
    @Autowired
    private CacheDao cacheDao;

    @RequestMapping(value = "/set")
    public String setCache(@RequestParam(name = "key") String key,
                           @RequestParam(name = "value") String value) {
        cacheDao.set(key, value);
        return super.getSuccessResult(1);
    }

    @RequestMapping(value = "/get")
    public String getCache(@RequestParam(name = "key") String key) {
        return super.getSuccessResult(cacheDao.get(key));
    }

    @RequestMapping(value = "/count")
    public String getCacheNum(@RequestParam(name = "pattern") String pattern) {
        return super.getSuccessResult(cacheDao.getKeys(pattern));
    }

    @RequestMapping(value = "/delete")
    public String deleteCache(@RequestParam(name = "key") String key) {
        if ("*".equals(key)) {
            return super.getSuccessResult(cacheDao.deleteAll());
        } else {
            return super.getSuccessResult(cacheDao.delete(key));
        }
    }
}
