package com.suny.tenant.mode.multiple.helper;

import com.suny.tenant.constant.TenantSupportConstant;
import com.suny.tenant.mode.multiple.provider.SystemTenantHelper;
import com.suny.tenant.thread.PropertiesContext;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 租户缓存 key
 *
 * @author sunjianrong
 * @date 2021-05-18 11:01
 */
@Slf4j
@Component
public class TenantCacheKeyHelper {

    @Autowired
    private SystemTenantHelper systemTenantHelper;


    public boolean containTenantKey(String originKey) {
        if (originKey.contains(TenantSupportConstant.SEPARATOR)) {
            final String[] split = originKey.split(TenantSupportConstant.SEPARATOR);
            if (split.length == 0) {
                return false;
            }
            final String s = split[0];
            return systemTenantHelper.isExist(s);
        }
        return false;
    }

    public String resolveTenantKey(String originKey) {
        if (StringUtils.isBlank(originKey)) {
            return originKey;
        }
        // feature function  20200529
        if (containTenantKey(originKey)) {
            return originKey;
        }

        final String tenantId = UserContext.getTenantId();
        final PropertiesContext.Properties properties = PropertiesContext.getProperties();
        if (properties.isIgnoreTenantId()) {
            return originKey;
        }

        if (StringUtils.isBlank(tenantId)) {
            log.warn("No tenantId was found , Please check thread context !");
            return originKey;
            // throw new IllegalArgumentException(tenantId);
        }

        return tenantId + ":" + originKey;
    }
}
