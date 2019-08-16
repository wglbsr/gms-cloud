package com.dyny.gdmodule.service.impl;

import com.dyny.gdmodule.db.entity.DataRule;
import com.dyny.gdmodule.db.dao.DataRuleMapper;
import com.dyny.gdmodule.service.DataRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        List<DataRule> dataRuleList = super.list();
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
}
