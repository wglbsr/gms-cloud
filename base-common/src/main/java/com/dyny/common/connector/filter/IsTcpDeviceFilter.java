package com.dyny.common.connector.filter;

import com.dyny.common.enums.ConnectionTypeEnum;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;

/**
 * @Auther: lane
 * @Date: 2019-03-29 16:04
 * @Description:
 * @Version 1.0.0
 */
public class IsTcpDeviceFilter implements ChannelContextFilter {
    @Override
    public boolean filter(ChannelContext channelContext) {
        Integer connectionType = (Integer) channelContext.getAttribute(ConnectionTypeEnum.KEY_CONNECTION_TYPE);
        return ConnectionTypeEnum.TCP_FROM_DEVICE.getType().equals(connectionType);
    }
}
