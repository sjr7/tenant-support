package com.suny.tenant.api.auth;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author sunjianrong
 * @date 2021-05-07 17:40
 */
@Slf4j
public abstract class AbstractLoginInfoContext implements ILoginInfoContext {

    @Override
    public boolean existLoginInfo() {
        try {
            final String tenantId = getTenantId();
            return StringUtils.isNotBlank(tenantId);
        } catch (Exception e) {
            return false;
        }
    }
}
