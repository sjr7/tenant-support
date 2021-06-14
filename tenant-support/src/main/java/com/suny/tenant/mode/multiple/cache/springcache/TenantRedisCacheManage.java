package com.suny.tenant.mode.multiple.cache.springcache;

import com.suny.tenant.mode.multiple.helper.TenantCacheKeyHelper;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Map;

/**
 * redis 缓存支持拓展
 *
 * @author sunjianrong
 * @date 2021-05-14 17:51
 */
public class TenantRedisCacheManage extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;
    private TenantCacheKeyHelper tenantCacheKeyHelper;

    @Override
    public Cache getCache(String name) {
        return super.getCache(tenantCacheKeyHelper.resolveTenantKey(name));
    }

    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter               must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *                                  {@link RedisCacheConfiguration#defaultCacheConfig()}.
     */
    public TenantRedisCacheManage(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter               must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *                                  {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheNames         optional set of known cache names that will be created with given
     *                                  {@literal defaultCacheConfiguration}.
     */
    public TenantRedisCacheManage(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter                must not be {@literal null}.
     * @param defaultCacheConfiguration  must not be {@literal null}. Maybe just use
     *                                   {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param allowInFlightCacheCreation if set to {@literal true} no new caches can be acquire at runtime but limited to
     *                                   the given list of initial cache names.
     * @param initialCacheNames          optional set of known cache names that will be created with given
     *                                   {@literal defaultCacheConfiguration}.
     * @since 2.0.4
     */
    public TenantRedisCacheManage(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }


    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter                must not be {@literal null}.
     * @param defaultCacheConfiguration  must not be {@literal null}. Maybe just use
     *                                   {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheConfigurations Map of known cache names along with the configuration to use for those caches.
     *                                   Must not be {@literal null}.
     */
    public TenantRedisCacheManage(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }


    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter                must not be {@literal null}.
     * @param defaultCacheConfiguration  must not be {@literal null}. Maybe just use
     *                                   {@link RedisCacheConfiguration#defaultCacheConfig()}.
     * @param initialCacheConfigurations Map of known cache names along with the configuration to use for those caches.
     *                                   Must not be {@literal null}.
     * @param allowInFlightCacheCreation if set to {@literal false} this cache manager is limited to the initial cache
     *                                   configurations and will not create new caches at runtime.
     * @since 2.0.4
     */
    public TenantRedisCacheManage(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

}
