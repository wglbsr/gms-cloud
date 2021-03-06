package com.dyny.basemongodb.controller;

import com.dyny.common.controller.BaseController;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @Auther: lane
 * @Date: 2019-03-07 15:14
 * @Description:
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/file", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class FileController extends BaseController {
    private static Log log = LogFactory.getLog(FileController.class);
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;


    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public String upload(@RequestParam(value = "file") MultipartFile file) {
        String fullName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fullName)) {
            return getErrorMsg("找不到文件名!");
        }
        String suffix = fullName.substring(fullName.lastIndexOf("."));
        DBObject dbObject = new BasicDBObject();
        dbObject.put("createdDate", new Date());
        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            ObjectId objectId = gridFsTemplate.store(inputStream, fileName, dbObject);
            String fileId = objectId.toString();
            return getSuccessResult(fileId);
        } catch (IOException e) {
            return getErrorMsg("上传失败!");
        }
    }


    /**
     * @return void
     * @Author wanggl(lane)
     * @Description //TODO 调用文件下载需要使用feign包中的Response接收返回结果
     * @Date 09:06 2019-04-08
     * @Param [fileId, response]
     **/
    @GetMapping(value = "/download/{fileId}")
    public void getFileById(@PathVariable("fileId") String fileId, HttpServletResponse response) throws IOException {
        Query query = Query.query(Criteria.where("_id").is(fileId));
        GridFSFile file = gridFsTemplate.findOne(query);
        if (file == null) {
            throw new RuntimeException("file not found:" + fileId);
        }
        GridFSDownloadStream in = gridFSBucket.openDownloadStream(file.getObjectId());
        GridFsResource resource = new GridFsResource(file, in);
        String fileName = file.getFilename().replace(",", "");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        response.setHeader("fileName", fileName);
        IOUtils.copy(resource.getInputStream(), response.getOutputStream());
    }


    @RequestMapping(value = "/delete")
    public String deleteFile(@RequestParam("fileId") String fileId) {
        Query query = Query.query(Criteria.where("_id").is(new ObjectId(fileId)));
        gridFsTemplate.delete(query);
        return getSuccessResult(1);
    }
}
