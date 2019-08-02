package com.dyny.common.connector.packet;

import com.dyny.common.annotation.Unfinished;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lane
 * @Date: 2019-04-01 16:25
 * @Description:
 * @Version 1.0.0
 */
@Unfinished
public class BizPacket {

    private Integer cmd;
    private Map<Integer, List<Byte>> content;
}
