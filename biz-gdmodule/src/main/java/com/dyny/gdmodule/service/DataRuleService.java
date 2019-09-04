package com.dyny.gdmodule.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dyny.gdmodule.db.entity.DataRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanggl
 * @since 2019-08-16
 */
public interface DataRuleService extends IService<DataRule> {
    Map<Integer, List<DataRule>> getAllDataRule();

    DataRule getOne(String key);

    List<DataRule> get(List<String> key);

    IPage<DataRule> getByCondition(String keyword, Map<String, Object> otherCons, int pageNum, int pageSize);

    boolean delete(String key);

    boolean delete(List<String> key);

    boolean create(DataRule dataRule);

    boolean update(DataRule dataRule);
}
