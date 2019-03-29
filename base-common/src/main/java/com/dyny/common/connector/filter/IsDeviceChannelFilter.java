package com.dyny.common.connector.filter;

import com.dyny.common.constant.TcpConstant;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;

/**
 * @Auther: lane
 * @Date: 2019-03-29 16:04
 * @Description:
 * @Version 1.0.0
 */
public class IsDeviceChannelFilter implements ChannelContextFilter {
    @Override
    public boolean filter(ChannelContext channelContext) {
        Boolean isBizChannel = (Boolean) channelContext.getAttribute(TcpConstant.KEY_IS_BIZ_CHANNEL);
        return isBizChannel == null || !isBizChannel;
    }
}
