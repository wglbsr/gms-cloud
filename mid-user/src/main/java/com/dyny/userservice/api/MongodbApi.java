package com.dyny.userservice.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: lane
 * @Date: 2019-03-11 11:42
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("base-mongodb")
public interface MongodbApi {

    @RequestMapping("/file/delete")
    String deleteFile(@RequestParam("fileId") String fileId);


    @GetMapping("/data/{table}/{id}")
    String select(@PathVariable("table") String tableName, @PathVariable("id") String id);


    @PostMapping("/data/{table}/{id}")
    String insert(@PathVariable("table") String tableName, @PathVariable("id") String id, @RequestParam("setting") String jsonData);

    @PostMapping("/data/{table}")
    String insert(@PathVariable("table") String tableName, @RequestParam("setting") String jsonData);

    @DeleteMapping("/data/{table}/{id}")
    String delete(@PathVariable("table") String tableName, @PathVariable("id") String id);


    @PutMapping("/data/{table}/{id}")
    String update(@PathVariable("table") String tableName, @PathVariable("id") String id, @RequestParam("setting") String jsonData);
}
