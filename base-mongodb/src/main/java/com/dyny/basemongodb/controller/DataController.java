package com.dyny.basemongodb.controller;

import com.dyny.basemongodb.dao.MongodbDao;
import com.dyny.common.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
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
        Document document = mongodbDao.find(tableName, translateStr(id, true));
        if (document == null) {
            return getSuccessResult(0);
        }
        return getSuccessResult(document.toJson());
    }

    private String translateStr(String str, boolean toHex) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        while (str.length() < 12) {
            str = "0" + str;
        }
        return toHex ? Hex.toHexString(str.getBytes()) : str;
    }


    @PostMapping("/{table}/{id}")
    public String insert(@PathVariable("table") String tableName, @PathVariable("id") String id, @RequestBody String jsonData) {
        return getSuccessResult(mongodbDao.insert(tableName, id, jsonData));
    }

    @PostMapping("/{table}")
    public String insert(@PathVariable("table") String tableName, @RequestBody String jsonData) {
        return getSuccessResult(mongodbDao.insert(tableName, jsonData));
    }

    @DeleteMapping("/{table}/{id}")
    public String delete(@PathVariable("table") String tableName, @PathVariable("id") String id) {
        return getSuccessResult(mongodbDao.delete(tableName, id));
    }


    @PutMapping("/{table}/{id}")
    public String update(@PathVariable("table") String tableName, @PathVariable("id") String id, @RequestBody String jsonData) {
        Document document = mongodbDao.update(tableName, id, jsonData);
        if (document != null) {
            return getSuccessResult(document.toJson());
        } else {
            return getErrorMsg("更新失败");
        }
    }


    @PutMapping("/{table}/{id}/str")
    public String updateStr(@PathVariable("table") String tableName, @PathVariable("id") String id, @RequestBody String jsonData) {
        String objectId = translateStr(id, true);
        Document document = mongodbDao.find(tableName, objectId);
        if (document == null) {
            objectId = mongodbDao.insert(tableName, objectId, jsonData);
        } else {
            document = mongodbDao.update(tableName, objectId, jsonData);
        }
        if (document != null) {
            return getSuccessResult(objectId);
        } else {
            return getErrorMsg("更新失败");
        }
    }

}
