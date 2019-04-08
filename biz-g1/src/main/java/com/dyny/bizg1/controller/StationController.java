package com.dyny.bizg1.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyny.bizg1.api.FileApi;
import com.dyny.bizg1.db.entity.GmsUser;
import com.dyny.bizg1.db.entity.Station;
import com.dyny.bizg1.service.GmsUserService;
import com.dyny.bizg1.service.StationService;
import com.dyny.common.controller.BaseController;
import com.dyny.common.utils.Utils;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@RestController
@RequestMapping(value = "/station", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class StationController extends BizBaseControllerT<Station> {
    @Autowired
    private StationService stationService;
    @Autowired
    private FileApi fileApi;

    @RequestMapping("/select/{id}")
    public String selectById(@PathVariable("id") int stationId) {
        return getSuccessResult(stationService.getById(stationId));
    }


    @RequestMapping("/select")
    public String select(@RequestParam(KEY_KEY_WORD) String keyWord,
                         @RequestParam(KEY_PAGE_NUM) int pageNum,
                         @RequestParam(KEY_PAGE_SIZE) int pageSize) {
        QueryWrapper<Station> stationQueryWrapper = new QueryWrapper<>();
        stationQueryWrapper.like("name", keyWord).or()
                .like("address", keyWord);
        return getSuccessPage(stationService.page(new Page<>(pageNum, pageSize), stationQueryWrapper));
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") int stationId) {
        return getSuccessResult(stationService.removeById(stationId));
    }


    @RequestMapping("/modify")
    public String modify(@RequestBody Station station) {
        return getSuccessResult(stationService.updateById(station));
    }

    @RequestMapping("/create")
    public String create(@RequestBody Station station) {
        return getSuccessResult(stationService.save(station) ? station.getId() : 0);
    }

    @Autowired
    GmsUserService gmsUserService;

    /**
     * @return java.lang.String
     * @Author wanggl(lane)
     * @Description //TODO 后续应该做成通用的方式
     * 1.上传到mongodb,获得文件id
     * 2.将文件id作为参数传到这个方法进行导入
     * 3.删除文件
     * @Date 16:41 2019-04-04
     * @Param [file]
     **/
    @RequestMapping("/import")
    @ResponseBody
    public String importStationDataByExcel(@RequestParam("fileId") String fileId, @RequestParam("suffix") String suffix) {
        Response response;
        InputStream inputStream;
        FileOutputStream fos;
        BufferedOutputStream bos;
        File file = null;
        Boolean result = false;
        try {
            response = fileApi.getFileById(fileId);
            Response.Body body = response.body();
            inputStream = body.asInputStream();
            byte[] b = new byte[inputStream.available()];
            file = new File(Utils.OS.getTempDir() + File.separator + fileId + suffix);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(b);
            Integer userId = getUserId();
            GmsUser gmsUser = gmsUserService.getById(userId);
            result = stationService.importStationFromExcelFile(file, gmsUser.getCustomerId());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileApi.deleteFile(fileId);
            if (file != null && file.exists()) {
                file.delete();
            }
        }


        return super.getSuccessResult(result);
    }
}

