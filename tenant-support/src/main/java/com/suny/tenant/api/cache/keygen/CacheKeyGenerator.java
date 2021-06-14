package com.suny.tenant.api.cache.keygen;

/**
 * 缓存 KEY 生成工具
 *
 * @author sunjianrong
 * @date 2021-05-11 15:22
 */
public interface CacheKeyGenerator {

    /**
     * 生成缓存 key
     *
     * @param originKey 原始 key
     * @return 处理过后 key
     */
    String genKey(String originKey);
}
