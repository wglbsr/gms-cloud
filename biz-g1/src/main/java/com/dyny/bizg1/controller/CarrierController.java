package com.dyny.bizg1.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyny.bizg1.db.entity.Carrier;
import com.dyny.bizg1.service.CarrierService;
import com.dyny.common.utils.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wanggl
 * @since 2019-03-12
 */
@RestController
@RequestMapping(value = "/Carrier", produces = {BaseController.ENCODE_CHARSET_UTF8})
public class CarrierController extends BaseController {
    @Autowired
    CarrierService carrierService;

    @RequestMapping("/select/{id}")
    public String selectById(@PathVariable("id") int carrierId) {
        return getSuccessResult(carrierService.getById(carrierId));
    }


    @RequestMapping("/select")
    public String select(@RequestParam(KEY_KEY_WORD) String keyWord,
                         @RequestParam(KEY_PAGE_NUM) int pageNum,
                         @RequestParam(KEY_PAGE_SIZE) int pageSize) {
        QueryWrapper<Carrier> carrierQueryWrapper = new QueryWrapper<>();
        carrierQueryWrapper.like("name", keyWord).or()
                .like("address", keyWord);
        return getSuccessResult(carrierService.page(new Page<>(pageNum, pageSize), carrierQueryWrapper));
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable("id") int carrierId) {
        return getSuccessResult(carrierService.removeById(carrierId));
    }


    @RequestMapping("/modify")
    public String modify(@RequestBody Carrier carrier) {
        return getSuccessResult(carrierService.updateById(carrier));
    }

    @RequestMapping("/create")
    public String create(@RequestBody Carrier carrier) {
        return getSuccessResult(carrierService.save(carrier) ? carrier.getId() : 0);
    }
}

