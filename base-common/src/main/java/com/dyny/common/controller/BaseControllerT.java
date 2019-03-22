package com.dyny.common.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @Author wanggl
 * @Description 通用方法
 * @create 10:21 2018-11-22
 * @modify  2019-01-24
 **/
public class BaseControllerT<T> extends BaseController {

    /**
     * @return java.lang.String
     * @Author wanggl(lane)
     * @Description //TODO mybatis plus 的page转换
     * @Date 10:08 2018-12-19
     * @Param [page]
     **/
    public String getSuccessPage(IPage<T> page) {
        return super.getSuccessResult(page.getRecords(), page.getCurrent(), page.getSize(), page.getTotal());
    }




}
