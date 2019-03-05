package com.dyny.baseredis.controller;

import com.dyny.baseredis.redis.CacheDao;
import com.dyny.common.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @Auther: lane
 * @Date: 2019-03-04 14:10
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping("/redis")
public class RedisController extends BaseController {
    @Autowired
    private CacheDao cacheDao;


    @RequestMapping("/get")
    public String get(@RequestParam("key") String key) {
        return getSuccessResult(cacheDao.get(key));
    }

    @RequestMapping("/set")
    public String set(@RequestParam("key") String key, @RequestParam("value") String value,
                      @RequestParam(value = "timeout", required = false, defaultValue = "0") int timeoutMin) {
        if (timeoutMin > 0) {
            cacheDao.set(key, value, timeoutMin, TimeUnit.MINUTES);
        } else {
            cacheDao.set(key, value);
        }
        return getSuccessResult(1);
    }

    @RequestMapping("/refresh")
    public String refresh(@RequestParam("key") String key,
                          @RequestParam("timeout") int timeoutMin) {
        cacheDao.updateTimeout(key, timeoutMin, TimeUnit.MINUTES);
        return getSuccessResult(1);
    }

}
