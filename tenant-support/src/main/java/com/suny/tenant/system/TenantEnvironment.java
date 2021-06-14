package com.suny.tenant.system;

import com.suny.tenant.enums.TenantMode;
import com.suny.tenant.mode.multiple.MultipleTenantModelConfiguration;
import com.suny.tenant.mode.single.SingleTenantModelConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunjianrong
 * @date 2021-05-18 11:10
 */
// @Component
@Slf4j
public final class TenantEnvironment {

    private final TenantApplicationContext tenantApplicationContext;

    public TenantEnvironment(TenantApplicationContext tenantApplicationContext) {
        this.tenantApplicationContext = tenantApplicationContext;
    }


    public static TenantMode getTenantMode() {
        try {
            final MultipleTenantModelConfiguration multipleTenantModelConfiguration = TenantApplicationContext.getBean(MultipleTenantModelConfiguration.class);
            if (multipleTenantModelConfiguration != null) {
                return TenantMode.MULTIPLE_TENANT_MODE;
            }
        } catch (Exception e) {
            return TenantMode.SINGLE_TENANT_MODE;
        }
        try {
            final SingleTenantModelConfiguration singleTenantModelConfiguration = TenantApplicationContext.getBean(SingleTenantModelConfiguration.class);
            if (singleTenantModelConfiguration != null) {
                return TenantMode.SINGLE_TENANT_MODE;
            }
        } catch (Exception e) {
            return TenantMode.MULTIPLE_TENANT_MODE;
        }

        return TenantMode.SINGLE_TENANT_MODE;
    }

}
