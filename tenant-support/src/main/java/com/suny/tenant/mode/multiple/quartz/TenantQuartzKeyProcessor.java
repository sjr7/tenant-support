package com.suny.tenant.mode.multiple.quartz;

import com.suny.tenant.api.quartz.QuartzKeyProcessor;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunjianrong
 * @date 2021-06-01 15:19
 */
// @Component
@Slf4j
public class TenantQuartzKeyProcessor implements QuartzKeyProcessor {

    private static final String SPLIT = "-";

    @Override
    public String processJobKey(String originJobKey) {
        if (originJobKey == null) {
            return null;
        }
        return UserContext.getTenantId() + SPLIT + originJobKey;
    }

    @Override
    public String processTriggerKey(String originJobKey) {
        if (originJobKey == null) {
            return null;
        }

        return UserContext.getTenantId() + SPLIT + originJobKey;
    }

    @Override
    public String processIdentityKey(String originIdentity) {
        if (originIdentity == null) {
            return null;
        }

        return UserContext.getTenantId() + SPLIT + originIdentity;
    }
}
