package com.dyny.bizg1.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyny.bizg1.db.dao.StationMapper;
import com.dyny.bizg1.db.entity.Station;
import com.dyny.bizg1.service.StationService;
import org.springframework.stereotype.Service;

import java.io.File;
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
    public boolean importStationFromExcelFile(File file, int customerId) {
        ImportParams params = new ImportParams();
        params.setTitleRows(0);
        params.setHeadRows(1);
        //Excel文件的所有编号
        List<Station> importList = ExcelImportUtil.importExcel(file, Station.class, params);
        for (Station station : importList) {
            station.setCustomerId(customerId);
            String code = station.getCode();
            station.setRegionId(Integer.valueOf(code.substring(0, 6)));
        }
        return saveOrUpdateBatch(importList);
    }
}
