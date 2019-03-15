package com.dyny.midregion;

import com.dyny.midregion.service.RegionService;
import com.dyny.midregion.utils.RegionDataCatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegionServiceApplicationTests {
    @Autowired
    RegionService regionService;

    @Test
    public void contextLoads() throws IOException {
        RegionDataCatcher regionDataCatcher = new RegionDataCatcher();
        regionDataCatcher.start(regionService);
    }

}
