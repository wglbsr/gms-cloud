package com.dyny.midregion.controller;


import com.alibaba.fastjson.JSONObject;
import com.dyny.common.utils.BaseController;
import com.dyny.common.utils.Utils;
import com.dyny.midregion.api.RedisApi;
import com.dyny.midregion.db.entity.Region;
import com.dyny.midregion.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-03-14
 */
@RestController
@RequestMapping("/region")
public class RegionController extends BaseController {
    @Autowired
    RegionService regionService;
    @Autowired
    RedisApi redisApi;

    @Value("${regionTimeout:1440}")
    private int regionTimeout;

    @RequestMapping("/select/{id}")
    public String select(@PathVariable("id") int id) {
        Region region = getData(redisApi.get(id + ""), Region.class);
        if (region == null) {
            region = regionService.getById(id);
            redisApi.set(id + "", JSONObject.toJSONString(region), regionTimeout);
        }
        return getSuccessResult(region);
    }


    @RequestMapping("/select")
    public String getByParentId(@RequestParam("parentId") int parentId, @RequestParam(value = "level", required = false, defaultValue = "0") int level) {
        return getSuccessResult(regionService.selectByParentId(parentId, level));
    }


    //    @RequestMapping("/select")
//    public String getByParentId(@RequestParam("parentIds[]") int[] parentIds) {
//        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
//        Arrays.sort(parentIds);
//        for (int index : parentIds) {
//
//        }
//
//        return getSuccessResult(regionService.list(regionQueryWrapper));
//    }
    @Value("${regionJsonFilePath:/Users/lane/Desktop}")
    private String tempPath;

    @RequestMapping("/json")
    public ResponseEntity test() throws IOException {
        String regionJson = regionService.getFullRegionJson();



        String fileName = "region.json";
        Utils.File.fileWrite(tempPath + File.separator + fileName, regionJson);
        return getFile(tempPath, fileName);
    }
}

