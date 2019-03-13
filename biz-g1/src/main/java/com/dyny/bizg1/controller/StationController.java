package com.dyny.bizg1.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyny.bizg1.db.entity.Station;
import com.dyny.bizg1.service.StationService;
import com.dyny.common.utils.BaseController;
import com.dyny.common.utils.BaseControllerT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
public class StationController extends BaseControllerT<Station> {
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
}

