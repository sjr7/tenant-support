package com.suny.tenant.mode.single.quartz;

import com.suny.tenant.api.quartz.QuartzKeyProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author sunjianrong
 * @date 2021-06-01 15:42
 */
@ConditionalOnMissingBean(value = {QuartzKeyProcessor.class})
@Component
@Slf4j
public class SingleTenantQuartzKeyProcessor implements QuartzKeyProcessor {

    @Override
    public String processJobKey(String originJobKey) {
        return originJobKey;
    }

    @Override
    public String processTriggerKey(String originJobKey) {
        return originJobKey;
    }

    @Override
    public String processIdentityKey(String originIdentity) {
        return originIdentity;
    }
}
