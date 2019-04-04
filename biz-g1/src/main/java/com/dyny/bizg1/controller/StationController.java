package com.dyny.bizg1.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyny.bizg1.db.entity.GmsUser;
import com.dyny.bizg1.db.entity.Station;
import com.dyny.bizg1.service.StationService;
import com.dyny.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

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
    StationService stationService;

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


    /**
     * @Author wanggl(lane)
     * @Description //TODO 后续应该做成通用的方式
     * 1.上传到mongodb,获得文件id
     * 2.将文件id作为参数传到这个方法进行导入
     *
     * @Date 16:41 2019-04-04
     * @Param [file]
     * @return java.lang.String
     **/
    @RequestMapping("/importStationDataByExcel")
    @ResponseBody
    public String importStationDataByExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return super.getErrorMsg("文件为空!");
        }
        String fileName = file.getOriginalFilename();
        String path = "";
        File fileAbsPath = new File(path + File.separator + fileName);
        //覆盖旧文件
        if (fileAbsPath.exists()) {
            fileAbsPath.delete();
        }
        if (!fileAbsPath.getParentFile().exists()) { //判断文件父目录是否存在
            fileAbsPath.getParentFile().mkdirs();
        } else {
            file.transferTo(fileAbsPath);
        }
        GmsUser user = getUser(GmsUser.class);
        int customerId = user.getCustomerId();
        return super.getSuccessResult(stationService.importStationFromExcelFile(fileAbsPath, customerId));
    }
}

