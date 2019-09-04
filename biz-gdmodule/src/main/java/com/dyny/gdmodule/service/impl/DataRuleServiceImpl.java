package com.dyny.gdmodule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dyny.gdmodule.db.entity.DataRule;
import com.dyny.gdmodule.db.dao.DataRuleMapper;
import com.dyny.gdmodule.service.DataRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanggl
 * @since 2019-08-16
 */
@Service
public class DataRuleServiceImpl extends ServiceImpl<DataRuleMapper, DataRule> implements DataRuleService {

    @Override
    public Map<Integer, List<DataRule>> getAllDataRule() {
        Map<Integer, List<DataRule>> dataRuleMap = new HashMap<>();
        List<DataRule> dataRuleList = list();
        dataRuleList.forEach(data -> {
            int communicateId = data.getCommunicateId();
            if (dataRuleMap.containsKey(communicateId)) {
                List<DataRule> tempList = dataRuleMap.get(communicateId);
                tempList.add(data);
            } else {
                List<DataRule> tempList = new ArrayList<>();
                tempList.add(data);
                dataRuleMap.put(communicateId, tempList);
            }
        });
        return dataRuleMap;
    }

    @Override
    public DataRule getOne(String key) {
        QueryWrapper<DataRule> dataRuleQueryWrapper = new QueryWrapper<>();
        dataRuleQueryWrapper.eq("key", key);
        return getOne(dataRuleQueryWrapper);
    }

    @Override
    public List<DataRule> get(List<String> key) {
        QueryWrapper<DataRule> dataRuleQueryWrapper = new QueryWrapper<>();
        dataRuleQueryWrapper.in("data_key", key);
        return list(dataRuleQueryWrapper);
    }

    /**
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.dyny.gdmodule.db.entity.DataRule>
     * @Author wanggl(lane)
     * @Description //TODO JDK8语法
     * @Date 14:26 2019-09-03
     * @Param [keyword, otherCons, pageNum, pageSize]
     **/
    @Override
    public IPage<DataRule> getByCondition(String keyword, Map<String, Object> otherCons, int pageNum, int pageSize) {
        QueryWrapper<DataRule> dataRuleQueryWrapper = new QueryWrapper<>();
        if (otherCons != null) {
            otherCons.forEach(dataRuleQueryWrapper::eq);
        }
        if (StringUtils.isNotEmpty(keyword)) {
            dataRuleQueryWrapper.and(f -> f.like("data_key", keyword)
                    .or().like("remark", keyword)
                    .or().like("communicate_id", keyword)
                    .or().like("prefix", keyword)
                    .or().like("factor", keyword)
                    .or().like("suffix", keyword));
        }
        return page(new Page<>(pageNum, pageSize), dataRuleQueryWrapper);
    }

    @Override
    public boolean delete(String key) {
        QueryWrapper<DataRule> dataRuleQueryWrapper = new QueryWrapper<>();
        dataRuleQueryWrapper.eq("data_key", key);
        return remove(dataRuleQueryWrapper);
    }

    @Override
    public boolean delete(List<String> key) {
        QueryWrapper<DataRule> dataRuleQueryWrapper = new QueryWrapper<>();
        dataRuleQueryWrapper.in("data_key", key);
        return remove(dataRuleQueryWrapper);
    }

    @Override
    public boolean create(DataRule dataRule) {
        return save(check(dataRule));
    }

    @Override
    public boolean update(DataRule dataRule) {

        return updateById(check(dataRule));
    }


    private DataRule check(DataRule dataRule) {
        Integer bitIndex = dataRule.getBitIndex();
        String factor = dataRule.getFactor();
        Integer targetClass = dataRule.getTargetClass();

        //布尔型
        if (bitIndex >= 0) {
            dataRule.setSize(1);
            dataRule.setOriClass(0);
            //因数无效
            dataRule.setFactorClass(-1);
            dataRule.setFactorClass(0);
            dataRule.setFactor(null);
            dataRule.setFactorCalcType(-1);
            //前缀后缀无效
            dataRule.setPrefix(null);
            dataRule.setSuffix(null);
        }

        //非字符型没有前缀后缀
        if (targetClass != 3) {
            dataRule.setPrefix(null);
            dataRule.setSuffix(null);
        } else {//字符型因数无效
            dataRule.setFactorClass(-1);
            dataRule.setFactorClass(0);
            dataRule.setFactor(null);
            dataRule.setFactorCalcType(-1);
        }


        if (StringUtils.isEmpty(factor)) {
            dataRule.setFactorClass(-1);
        }


        return dataRule;
    }
}
