package com.suny.tenant.mode.multiple.quartz.config;

import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.thread.UserContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author sunjianrong
 * @date 2021-05-29 13:52
 */
public class TenantSchedulerFactoryBean extends SchedulerFactoryBean {

    private final TenantProperties dsProperties;

    public TenantSchedulerFactoryBean(TenantProperties tenantProperties) {
        this.dsProperties = tenantProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            UserContext.setTenantId(dsProperties.getDefaultTenantId());
            super.afterPropertiesSet();
        } finally {
            UserContext.clear();
        }
    }
}
