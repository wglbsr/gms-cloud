package com.dyny.bizg1.service;

import com.dyny.bizg1.db.entity.Station;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.File;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-12
 */
public interface StationService extends IService<Station> {
    List<Integer> importStationFromExcelFile(File file, int customerId);
}
