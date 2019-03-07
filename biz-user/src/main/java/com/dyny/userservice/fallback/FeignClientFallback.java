package com.dyny.userservice.fallback;

import com.dyny.userservice.api.RegionApi;

/**
 * @Auther: lane
 * @Date: 2019-03-07 11:23
 * @Description:
 * @Version 1.0.0
 */
public class FeignClientFallback implements RegionApi {
    @Override
    public String testRegion() {
        return null;
    }
}
