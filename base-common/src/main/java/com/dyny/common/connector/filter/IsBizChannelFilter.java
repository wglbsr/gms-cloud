package com.dyny.common.connector.filter;

import com.dyny.common.constant.TcpConstant;
import com.dyny.common.enums.ConnectionTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;

import java.util.Map;

/**
 * @Auther: lane
 * @Date: 2019-03-29 16:04
 * @Description:
 * @Version 1.0.0
 */
public class IsBizChannelFilter implements ChannelContextFilter {
    private String appName = null;
    private String key = null;
    private Object value = null;
    private Integer connectionType;

    public IsBizChannelFilter() {

    }

    public IsBizChannelFilter(ConnectionTypeEnum connectionType, String appName, Map<String, Object> attrs) {
        this.connectionType = connectionType.getType();
    }

    public IsBizChannelFilter(String appName) {
        this.appName = appName;
    }


    public IsBizChannelFilter(String key, Object value) {
        this.key = key;
        this.value = value;
    }


    @Override
    public boolean filter(ChannelContext channelContext) {
        Integer connectionType = (Integer) channelContext.getAttribute(ConnectionTypeEnum.KEY_CONNECTION_TYPE);
        boolean appNameMatch = true;
        if (StringUtils.isNotEmpty(appName)) {
            String appName = (String) channelContext.getAttribute(TcpConstant.KEY_APP_NAME);
            appNameMatch = this.appName.equals(appName);
        }
        return ConnectionTypeEnum.WS_FROM_SERVER.getType().equals(connectionType) && appNameMatch;
    }
}
