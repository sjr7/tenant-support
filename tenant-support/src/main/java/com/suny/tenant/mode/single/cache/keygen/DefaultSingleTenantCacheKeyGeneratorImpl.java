package com.suny.tenant.mode.single.cache.keygen;

import com.suny.tenant.api.cache.keygen.CacheKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author sunjianrong
 * @date 2021-05-13 11:21
 */
@ConditionalOnMissingBean(value = {CacheKeyGenerator.class})
@Component
@Slf4j
public class DefaultSingleTenantCacheKeyGeneratorImpl implements CacheKeyGenerator {
    @Override
    public String genKey(String originKey) {
        return originKey;
    }
}
