package com.dyny.basemongodb.controller;

import com.dyny.basemongodb.dao.MongodbDao;
import com.dyny.common.controller.BaseController;
import org.bouncycastle.util.encoders.Hex;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private MongodbDao mongodbDao;

    @GetMapping("/{table}/{id}")
    public String select(@PathVariable("table") String tableName, @PathVariable("id") String id) {
        Document document = mongodbDao.find(tableName, id);
        if (document == null) {
            return getSuccessResult(0);
        }
        return getSuccessResult(document.toJson());
    }
    @GetMapping("/{table}/{id}/hex")
    public String selectByHex(@PathVariable("table") String tableName, @PathVariable("id") String id) {
        Document document = mongodbDao.find(tableName, id);
        if (document == null) {
            return getSuccessResult(0);
        }
        return getSuccessResult(document.toJson());
    }

    @GetMapping("/{table}/{id}/num")
    public String selectByNum(@PathVariable("table") String tableName, @PathVariable("id") String id) {
        while (id.length() < 12) {
            id = "0" + id;
        }
        Document document = mongodbDao.find(tableName, Hex.toHexString(id.getBytes()));
        if (document == null) {
            return getSuccessResult(0);
        }
        return getSuccessResult(document.toJson());
    }


    @PostMapping("/{table}/{id}")
    public String insert(@PathVariable("table") String tableName, @PathVariable("id") String id, @RequestParam("setting") String jsonData) {
        return getSuccessResult(mongodbDao.insert(tableName, id, jsonData));
    }

    @PostMapping("/{table}")
    public String insert(@PathVariable("table") String tableName, @RequestParam("setting") String jsonData) {
        return getSuccessResult(mongodbDao.insert(tableName, jsonData));
    }

    @DeleteMapping("/{table}/{id}")
    public String delete(@PathVariable("table") String tableName, @PathVariable("id") String id) {
        return getSuccessResult(mongodbDao.delete(tableName, id));
    }


    @PutMapping("/{table}/{id}")
    public String update(@PathVariable("table") String tableName, @PathVariable("id") String id, @RequestParam("setting") String jsonData) {
        return getSuccessResult(mongodbDao.update(tableName, id, jsonData).toJson());
    }


}
