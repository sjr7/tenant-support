package com.suny.tenant.mode.multiple.provider;

import com.suny.tenant.system.tenant.SysTenant;
import com.suny.tenant.system.tenant.SysTenantService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-05-06 14:18
 */
// @Service
@Slf4j
// @ConditionalOnMissingBean(value = {TenantProviderService.class})
public class DefaultTenantProviderServiceImpl implements TenantProviderService {
    private final SysTenantService sysTenantService;

    public DefaultTenantProviderServiceImpl(SysTenantService sysTenantService) {
        this.sysTenantService = sysTenantService;
    }

    @Override
    public List<SysTenant> loadAll() {
        return sysTenantService.selectAll();
    }
}
