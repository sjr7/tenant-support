package com.suny.tenant.mode.multiple.provider;

import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.system.tenant.SysTenant;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunjianrong
 * @date 2021-05-06 17:04
 */
@Component
@Slf4j
// @DependsOn("systemDataInit")
// @DependsOn("tenantProviderService")
public final class SystemTenantHelper {

    private static final Map<String, SysTenant> TENANT_MAP = new ConcurrentHashMap<>();

    private final TenantProperties properties;

    public SystemTenantHelper(TenantProviderService tenantProviderService, TenantProperties properties) {
        try {
            UserContext.setTenantId(properties.getDefaultTenantId());
            log.info("init tenant data!");
            final List<SysTenant> tenants = tenantProviderService.loadAll();
            tenants.forEach(tenant -> TENANT_MAP.put(tenant.getTenantId(), tenant));

        } finally {
            UserContext.clear();
        }


        this.properties = properties;
    }


    public boolean isExist(String tenantId) {
        return TENANT_MAP.containsKey(tenantId);
    }


    public List<SysTenant> getAll() {
        if (TENANT_MAP.size() == 0) {
            log.warn("Available tenant list is empty! will be loadAll !");
        }
        return new ArrayList<>(TENANT_MAP.values());
    }

    public SysTenant getSystemTenant() {
        final SysTenant sysTenant = getAll().stream().filter(e -> e.getSystemTenant() != null && e.getSystemTenant()).findFirst().orElse(null);
        if (sysTenant == null) {
            log.warn("System tenant not exist!");
        }

        return sysTenant;
    }

    public SysTenant getTenant(String tenantId) {
        if (isExist(tenantId)) {
            return TENANT_MAP.get(tenantId);
        } else {
            return null;
        }
    }

    public void addTenant(String tenantId, SysTenant sysTenant) {
        TENANT_MAP.put(tenantId, sysTenant);
    }

    public void removeTenant(String tenantId) {
        TENANT_MAP.remove(tenantId);
    }

    public void updateTenant(String tenantId, SysTenant sysTenant) {
        TENANT_MAP.put(tenantId, sysTenant);
    }

}
