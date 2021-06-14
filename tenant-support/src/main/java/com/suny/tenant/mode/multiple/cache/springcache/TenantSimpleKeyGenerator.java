package com.suny.tenant.mode.multiple.cache.springcache;

import com.suny.tenant.mode.multiple.helper.TenantCacheKeyHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;

/**
 * 简单 key 生成器
 *
 * @author sunjianrong
 * @date 2021-05-14 15:00
 */
// @Component
@Slf4j
public class TenantSimpleKeyGenerator extends SimpleKeyGenerator {

    private final TenantCacheKeyHelper tenantCacheKeyHelper;

    public TenantSimpleKeyGenerator(TenantCacheKeyHelper tenantCacheKeyHelper) {
        this.tenantCacheKeyHelper = tenantCacheKeyHelper;
    }

    @Override
    public Object generate(Object target, Method method, Object... params) {
        final Object generate = super.generate(target, method, params);
        return tenantCacheKeyHelper.resolveTenantKey(String.valueOf(generate));
    }
}
