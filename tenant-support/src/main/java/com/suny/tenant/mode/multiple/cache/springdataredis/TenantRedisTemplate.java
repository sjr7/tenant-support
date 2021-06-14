package com.suny.tenant.mode.multiple.cache.springdataredis;

import org.springframework.data.redis.core.*;
import org.springframework.data.redis.hash.HashMapper;

/**
 * @author sunjianrong
 * @date 2021-05-19 11:20
 */
public class TenantRedisTemplate<K, V> extends RedisTemplate<K, V> {

    // private final ValueOperations<K, V> valueOps = new DefaultValueOperations<>(this);
    // private final ListOperations<K, V> listOps = new DefaultListOperations<>(this);
    // private final SetOperations<K, V> setOps = new DefaultSetOperations<>(this);
    // private final StreamOperations<K, ?, ?> streamOps = new DefaultStreamOperations<>(this, new ObjectHashMapper());
    // private final ZSetOperations<K, V> zSetOps = new DefaultZSetOperations<>(this);
    // private final GeoOperations<K, V> geoOps = new DefaultGeoOperations<>(this);
    // private final HyperLogLogOperations<K, V> hllOps = new DefaultHyperLogLogOperations<>(this);
    // private final ClusterOperations<K, V> clusterOps = new DefaultClusterOperations<>(this);


    private final ValueOperations<K, V> valueOps = null;
    private final ListOperations<K, V> listOps = null;
    private final SetOperations<K, V> setOps = null;
    private final StreamOperations<K, ?, ?> streamOps = null;
    private final ZSetOperations<K, V> zSetOps = null;
    private final GeoOperations<K, V> geoOps = null;
    private final HyperLogLogOperations<K, V> hllOps = null;
    private final ClusterOperations<K, V> clusterOps = null;


    @Override
    public ClusterOperations<K, V> opsForCluster() {
        return super.opsForCluster();
    }

    @Override
    public GeoOperations<K, V> opsForGeo() {
        return super.opsForGeo();
    }

    @Override
    public <HK, HV> HashOperations<K, HK, HV> opsForHash() {
        return super.opsForHash();
    }

    @Override
    public HyperLogLogOperations<K, V> opsForHyperLogLog() {
        return super.opsForHyperLogLog();
    }

    @Override
    public ListOperations<K, V> opsForList() {
        return super.opsForList();
    }

    @Override
    public SetOperations<K, V> opsForSet() {
        return super.opsForSet();
    }

    @Override
    public <HK, HV> StreamOperations<K, HK, HV> opsForStream() {
        return super.opsForStream();
    }

    @Override
    public <HK, HV> StreamOperations<K, HK, HV> opsForStream(HashMapper<? super K, ? super HK, ? super HV> hashMapper) {
        return super.opsForStream(hashMapper);
    }

    @Override
    public ValueOperations<K, V> opsForValue() {
        return super.opsForValue();
    }

    @Override
    public ZSetOperations<K, V> opsForZSet() {
        return super.opsForZSet();
    }
}
