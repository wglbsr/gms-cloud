package com.dyny.common.db;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * @Author wanggl
 * @Description 存放通用静态方法
 * @create 11:09 2018-11-23
 * @modify 11:09 2018-11-23
 **/
public interface CommonMapper<T> extends BaseMapper<T> {
    static boolean saveOrUpdateAuto(Collection collection) {


        return false;
    }

}
