package com.dyny.bizg1.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyny.bizg1.db.dao.StationMapper;
import com.dyny.bizg1.db.entity.Station;
import com.dyny.bizg1.service.StationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

    @Override
    public List<Integer> importStationFromExcelFile(File file, int customerId) {
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        //Excel文件的所有编号
        List<Station> importList = ExcelImportUtil.importExcel(file, Station.class, params);
        List<Station> insertList = new ArrayList<>();
        int index = 0;
        List<Integer> errorList = new ArrayList<>();
        for (Station station : importList) {
            index++;
            String code = station.getCode();
            String address = station.getAddress();
            Integer type = station.getType();
            if (!StringUtils.isAnyEmpty(code, address) && type != null && type > 0) {
                station.setCustomerId(customerId);
                station.setRegionId(Integer.valueOf(code.substring(0, 6)));
                insertList.add(station);
            } else {
                errorList.add(index);
            }
        }
        if (!saveOrUpdateBatch(insertList)) {
            errorList.clear();
            errorList.add(-1);
        }
        return errorList;
    }
}
