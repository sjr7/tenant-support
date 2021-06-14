package com.suny.tenant.mode.multiple.cache.keygen;

import com.suny.tenant.api.cache.keygen.CacheKeyGenerator;
import com.suny.tenant.mode.multiple.helper.TenantCacheKeyHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 默认缓存 KEY 生成策略
 *
 * @author sunjianrong
 * @date 2021-05-11 15:24
 */
// @ConditionalOnMissingBean(value = {CacheKeyGenerator.class})
// @Component
@Slf4j
public class DefaultMultipleTenantCacheKeyGeneratorImpl implements CacheKeyGenerator {

    private final TenantCacheKeyHelper tenantCacheKeyHelper;

    public DefaultMultipleTenantCacheKeyGeneratorImpl(TenantCacheKeyHelper tenantCacheKeyHelper) {
        this.tenantCacheKeyHelper = tenantCacheKeyHelper;
    }

    @Override
    public String genKey(String originKey) {
        if (StringUtils.isBlank(originKey)) {
            return originKey;
        }

        return tenantCacheKeyHelper.resolveTenantKey(originKey);
    }
}
