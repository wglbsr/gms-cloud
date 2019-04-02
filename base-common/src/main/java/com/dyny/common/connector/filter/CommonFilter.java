package com.dyny.common.connector.filter;

import com.dyny.common.constant.TcpConstant;
import com.dyny.common.enums.ConnectionTypeEnum;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: lane
 * @Date: 2019-03-29 16:04
 * @Description:
 * @Version 1.0.0
 */
public class CommonFilter implements ChannelContextFilter {
    private String appName = null;
    private Integer connectionType;
    private Map<String, Object> attrs;

    public CommonFilter(ConnectionTypeEnum connectionType, String appName, Map<String, Object> attrs) {
        this.connectionType = connectionType.getType();
        this.attrs = attrs;
        this.attrs.put(TcpConstant.KEY_APP_NAME, appName);
        this.attrs.put(ConnectionTypeEnum.KEY_CONNECTION_TYPE, this.connectionType);
    }

    public CommonFilter(ConnectionTypeEnum connectionType, String appName) {
        this.connectionType = connectionType.getType();
        this.attrs = new HashMap<>();
        this.attrs.put(TcpConstant.KEY_APP_NAME, appName);
        this.attrs.put(ConnectionTypeEnum.KEY_CONNECTION_TYPE, this.connectionType);
    }

    public CommonFilter(String appName, Map<String, Object> attrs) {
        this.attrs = attrs;
        this.attrs.put(TcpConstant.KEY_APP_NAME, appName);
        this.attrs.put(ConnectionTypeEnum.KEY_CONNECTION_TYPE, null);
    }

    public CommonFilter(String appName) {
        this.attrs = new HashMap<>();
        this.attrs.put(TcpConstant.KEY_APP_NAME, appName);
    }

    public CommonFilter(Map<String, Object> attrs) {
        this.attrs = attrs;
        this.attrs.put(TcpConstant.KEY_APP_NAME, appName);
        this.attrs.put(ConnectionTypeEnum.KEY_CONNECTION_TYPE, null);
    }

    @Override
    public boolean filter(ChannelContext channelContext) {
        return isAllMatch(channelContext, this.attrs);
    }


    private boolean attrMatch(ChannelContext channelContext, String key, Object value) {
        Object channelVal = channelContext.getAttribute(key);
        return channelVal.equals(value);
    }

    private boolean isAllMatch(ChannelContext channelContext, Map<String, Object> attrs) {
        if (attrs == null || attrs.size() == 0) {
            return true;
        }
        Set<String> keySet = attrs.keySet();
        for (String key : keySet) {
            Object val = attrs.get(key);
            if (!attrMatch(channelContext, key, val)) {
                return false;
            }
        }
        return true;
    }
}
