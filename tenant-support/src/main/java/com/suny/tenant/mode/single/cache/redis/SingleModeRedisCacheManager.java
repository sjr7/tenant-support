package com.suny.tenant.mode.single.cache.redis;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Map;

/**
 * @author sunjianrong
 * @date 2021-05-18 11:07
 */
public class SingleModeRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;
    
    
    /**
     * Creates new {@link RedisCacheManager} using given {@link RedisCacheWriter} and default
     * {@link RedisCacheConfiguration}.
     *
     * @param cacheWriter               must not be {@literal null}.
     * @param defaultCacheConfiguration must not be {@literal null}. Maybe just use
     *                                  {@link RedisCacheConfiguration#defaultCacheConfig()}.
     */
    public SingleModeRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
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
    public SingleModeRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
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
    public SingleModeRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
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
    public SingleModeRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
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
    public SingleModeRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }
}
