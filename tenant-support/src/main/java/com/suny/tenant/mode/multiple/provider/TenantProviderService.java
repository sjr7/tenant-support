package com.suny.tenant.mode.multiple.provider;

import com.suny.tenant.system.tenant.SysTenant;

import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-05-06 10:29
 */
public interface TenantProviderService {

    List<SysTenant> loadAll();
}
