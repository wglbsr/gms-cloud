package com.dyny.midregion.service;

import com.dyny.midregion.db.entity.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-14
 */
public interface RegionService extends IService<Region> {
    List<Region> selectByParentId(int parentId, int level);

    String getFullRegionJson();
}
