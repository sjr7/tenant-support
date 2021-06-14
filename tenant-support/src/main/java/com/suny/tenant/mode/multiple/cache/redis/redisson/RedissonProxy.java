package com.suny.tenant.mode.multiple.cache.redis.redisson;

import com.suny.tenant.mode.multiple.helper.TenantCacheKeyHelper;
import com.suny.tenant.mode.multiple.proxy.AbstractSimpleParamProxy;
import com.suny.tenant.mode.multiple.proxy.ProxyMetaDataMap;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sunjianrong
 * @date 2021-05-21 17:02
 */
@ConditionalOnClass(RedissonClient.class)
@Configuration
@Slf4j
public class RedissonProxy extends AbstractSimpleParamProxy {
    private static final String CREATE_BATCH = "createBatch";

    @Autowired
    private TenantCacheKeyHelper tenantCacheKeyHelper;

    @Override
    public ProxyMetaDataMap initMetaDataMap() {
        Map<String, Class<?>> proxyRefMethod = new HashMap<>();
        proxyRefMethod.put(CREATE_BATCH, RBatch.class);
        return new ProxyMetaDataMap(RedissonClient.class, proxyRefMethod);
    }


    @Override
    public void paramHandler(Object[] args, int currentParamIndex, String currentParamName, Object currentParamValue) {
        if ("name".equalsIgnoreCase(currentParamName)) {
            final String resolveTenantKey = tenantCacheKeyHelper.resolveTenantKey(String.valueOf(args[0]));
            log.debug("single key {} > {}", args[0], resolveTenantKey);
            args[currentParamIndex] = resolveTenantKey;
        }
    }


    @Override
    public boolean beforeParamHandler(String methodName, String[] parameterNames, Object[] args) {
        return true;
    }

    @Override
    public void afterParamHandler(String methodName, String[] parameterNames, Object[] args) {
    }
}
