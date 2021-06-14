package com.suny.tenant.mode.multiple.ds.notify;

import com.suny.tenant.mode.multiple.provider.SystemTenantHelper;
import com.suny.tenant.system.tenant.SysTenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author sunjianrong
 * @date 2021-05-06 17:42
 */
@Component
@Slf4j
public class TenantNotify {

    private final com.suny.tenant.mode.multiple.provider.SystemTenantHelper SystemTenantHelper;

    public TenantNotify(SystemTenantHelper SystemTenantHelper) {
        this.SystemTenantHelper = SystemTenantHelper;
    }

    public void add(SysTenant sysTenant) {
        log.info("add tenant [{}-{}]", sysTenant.getTenantName(), sysTenant.getTenantId());
        SystemTenantHelper.addTenant(sysTenant.getTenantId(), sysTenant);
    }

    public void remove(String tenantId) {
        final boolean exist = SystemTenantHelper.isExist(tenantId);
        if (exist) {
            final SysTenant tenant = SystemTenantHelper.getTenant(tenantId);
            log.info("remove tenant [{}-{}]", tenant.getTenantName(), tenant.getTenantId());
            SystemTenantHelper.removeTenant(tenantId);
        } else {
            log.info("tenant [{}] not exist", tenantId);
        }
    }

    public void update(SysTenant sysTenant) {
        log.info("update tenant [{}-{}]", sysTenant.getTenantName(), sysTenant.getTenantId());
        SystemTenantHelper.updateTenant(sysTenant.getTenantId(), sysTenant);
    }
}
