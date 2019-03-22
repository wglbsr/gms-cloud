package com.dyny.basemongodb.controller;

import com.dyny.common.controller.BaseController;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lane
 * @Date: 2019-03-07 15:14
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/data", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class DataController extends BaseController {
    @Autowired
    MongoDbFactory mongoDbFactory;

    public String select(String keyWord) {
        MongoDatabase mongoDatabase = mongoDbFactory.getDb();
        return getSuccessResult(1);
    }


}
