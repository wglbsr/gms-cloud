package com.dyny.gdmodule.controller;


import com.dyny.common.controller.BaseControllerT;
import com.dyny.gdmodule.db.entity.DataRule;
import com.dyny.gdmodule.service.DataRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wanggl
 * @since 2019-08-16
 */
@RestController
@RequestMapping(value = "/dataRule", produces = {"application/json;charset=UTF-8"})
public class DataRuleController extends BaseControllerT<DataRule> {
    @Autowired
    DataRuleService dataRuleService;

    @CrossOrigin
    @GetMapping("/{dataKey}")
    public String get(@PathVariable("dataKey") String key) {
        return getSuccessResult(dataRuleService.getOne(key));
    }

    @CrossOrigin
    @GetMapping("/page")
    public String page(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                       @RequestParam("pageNum") int pageNum,
                       @RequestParam("pageSize") int pageSize) {
        return getSuccessPage(dataRuleService.getByCondition(keyword, null, pageNum, pageSize));
    }

    @CrossOrigin
    @DeleteMapping("/{dataKey}")
    public String delete(@PathVariable("dataKey") String key) {
        return getSuccessResult(dataRuleService.delete(key));
    }

    @CrossOrigin
    @PostMapping("/delete")
    public String delete(@RequestBody List<String> keys) {
        return getSuccessResult(dataRuleService.delete(keys));
    }

    @CrossOrigin
    @PostMapping("/create")
    public String create(@RequestBody DataRule dataRule) {
        return getSuccessResult(dataRuleService.create(dataRule));
    }

    @CrossOrigin
    @PutMapping("/update")
    public String update(@RequestBody DataRule dataRule) {
        return getSuccessResult(dataRuleService.update(dataRule));
    }


}

