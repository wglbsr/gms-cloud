package com.dyny.gdmodule.controller;


import com.dyny.common.controller.BaseController;
import com.dyny.common.controller.BaseControllerT;
import com.dyny.gdmodule.db.entity.DataRule;
import com.dyny.gdmodule.service.DataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-08-16
 */
@CrossOrigin(origins = "http://localhost:9528")
@RestController
@RequestMapping("/dataRule")
public class DataRuleController extends BaseControllerT<DataRule> {
    @Autowired
    DataRuleService dataRuleService;

    @GetMapping("/{dataKey}")
    public String get(@PathVariable("dataKey") String key) {
        return getSuccessResult(dataRuleService.getOne(key));
    }

    @GetMapping("/page")
    public String page(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                       @RequestParam("pageNum") int pageNum,
                       @RequestParam("pageSize") int pageSize) {
        return getSuccessPage(dataRuleService.getByCondition(keyword, null, pageNum, pageSize));
    }

    @DeleteMapping("/{dataKey}")
    public String delete(@PathVariable("dataKey") String key) {
        return getSuccessResult(dataRuleService.delete(key));
    }

    @DeleteMapping("/")
    public String delete(@RequestParam("dataKeys[]") List<String> keys) {
        return getSuccessResult(dataRuleService.delete(keys));
    }


    @PostMapping("/create")
    public String create(@RequestBody DataRule dataRule) {
        return getSuccessResult(dataRuleService.create(dataRule));
    }

    @PutMapping("/update")
    public String update(@RequestBody DataRule dataRule) {
        return getSuccessResult(dataRuleService.update(dataRule));
    }


}

