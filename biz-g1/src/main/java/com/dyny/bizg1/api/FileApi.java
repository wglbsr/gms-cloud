package com.dyny.bizg1.api;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Auther: lane
 * @Date: 2019-04-08 09:00
 * @Description:
 * @Version 1.0.0
 */
@FeignClient("base-mongodb")
public interface FileApi {

    @PostMapping(value = "file/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    String upload(@RequestParam(value = "file") MultipartFile file);

    /**
     * @Author wanggl(lane)
     * @Description //TODO 调用文件下载需要使用feign包中的Response接收返回结果
     * @Date 09:06 2019-04-08
     * @Param [fileId, response]
     * @return feign.Response
     **/
    @GetMapping(value = "file/download/{fileId}")
    Response getFileById(@PathVariable("fileId") String fileId) throws IOException;


    @RequestMapping(value = "file/delete")
    String deleteFile(@RequestParam("fileId") String fileId);
}
