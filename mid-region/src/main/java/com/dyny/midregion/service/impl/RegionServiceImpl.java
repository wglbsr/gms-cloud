package com.dyny.midregion.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dyny.midregion.db.dao.RegionMapper;
import com.dyny.midregion.db.entity.Region;
import com.dyny.midregion.service.RegionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wanggl
 * @since 2019-03-14
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {
    @Override
    public List<Region> selectByParentId(int parentId, int level) {
        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq("parent_id", parentId);
        if (level > 0) {
            regionQueryWrapper.eq("level", level);
        }
        return list(regionQueryWrapper);
    }

    @Override
    public String getFullRegionJson() {
        int maxLevel = 4;
        List<Region> level0 = selectByParentId(0, 1);

        return null;
    }
}
