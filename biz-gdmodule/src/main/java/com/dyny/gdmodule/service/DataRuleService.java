package com.dyny.gdmodule.service;

import com.dyny.gdmodule.db.entity.DataRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wanggl
 * @since 2019-08-16
 */
public interface DataRuleService extends IService<DataRule> {
    Map<Integer, List<DataRule>> getAllDataRule();
}
