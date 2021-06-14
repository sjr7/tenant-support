package com.suny.tenant.mode.multiple;

import com.suny.tenant.mode.multiple.ds.notify.DataSourceNotify;
import com.suny.tenant.mode.multiple.ds.notify.TenantNotify;
import com.suny.tenant.mode.multiple.ds.provider.DataSourceProviderService;
import com.suny.tenant.mode.multiple.provider.TenantProviderService;
import com.suny.tenant.system.datasource.SysDataSource;
import com.suny.tenant.system.tenant.SysTenant;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-22 14:20
 */
// @Component
@Slf4j
// public class SystemDataInitEvent implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
public class SystemDataInit {

    @Autowired
    private DataSourceProviderService dataSourceProviderService;

    @Autowired
    private TenantProviderService tenantProviderService;


    @Autowired
    private DataSourceNotify dataSourceNotify;

    @Autowired
    private TenantNotify tenantNotify;

    @Autowired
    private TenantProperties properties;


    // @PostConstruct
    public void init() {
        try {
            log.debug("init datasource !");
            UserContext.setTenantId(properties.getDefaultTenantId());
            final List<SysDataSource> sysDataSources = dataSourceProviderService.loadAll();
            for (SysDataSource sysDataSource : sysDataSources) {
                dataSourceNotify.add(sysDataSource);
            }

            final List<SysTenant> tenants = tenantProviderService.loadAll();
            for (SysTenant tenant : tenants) {
                tenantNotify.add(tenant);
            }

        } finally {
            UserContext.clear();
        }

    }

}
