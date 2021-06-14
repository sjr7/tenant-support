package com.suny.tenant.mode.multiple.cache.springdataredis;

import com.suny.tenant.mode.multiple.helper.TenantCacheKeyHelper;
import com.suny.tenant.mode.multiple.proxy.AbstractSimpleParamProxy;
import com.suny.tenant.mode.multiple.proxy.ProxyMetaDataMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.*;

import java.util.*;

/**
 * @author sunjianrong
 * @date 2021-05-24 19:50
 */
@ConditionalOnClass(RedisOperations.class)
@Configuration
@Slf4j
public class RedisTemplateProxy extends AbstractSimpleParamProxy {

    private static final String KEY = "key";
    private static final String KEY_START_WITH_CAPITAL = "Key";
    private static final String KEYS = "keys";
    private static final String KEYS_START_WITH_CAPITAL = "Keys";
    private static final String MAP = "map";
    private static final String MAP_KEY_ALIAS1 = "m";

    @Autowired
    private TenantCacheKeyHelper tenantCacheKeyHelper;

    @Override
    public ProxyMetaDataMap initMetaDataMap() {

        Set<String> basicMethodName = new HashSet<>();
        basicMethodName.add("hasKey");
        basicMethodName.add("countExistingKeys");
        basicMethodName.add("delete");
        basicMethodName.add("unlink");
        basicMethodName.add("type");
        basicMethodName.add("keys");
        basicMethodName.add("rename");
        basicMethodName.add("renameIfAbsent");
        basicMethodName.add("expire");
        basicMethodName.add("expireAt");
        basicMethodName.add("persist");
        basicMethodName.add("move");
        basicMethodName.add("dump");
        basicMethodName.add("restore");
        basicMethodName.add("getExpire");
        basicMethodName.add("sort");        // 不支持
        basicMethodName.add("watch");


        Map<String, Class<?>> refMethod = new HashMap<>();
        refMethod.put("opsForValue", ValueOperations.class);
        refMethod.put("opsForList", ListOperations.class);
        refMethod.put("opsForSet", SetOperations.class);
        // vending版本不支持
        // refMethod.put("opsForStream", StreamOperations.class);
        refMethod.put("opsForZSet", ZSetOperations.class);
        refMethod.put("opsForGeo", GeoOperations.class);
        refMethod.put("opsForHyperLogLog", HyperLogLogOperations.class);
        refMethod.put("opsForCluster", ClusterOperations.class);

        // refMethod.put("boundGeoOps",BoundGeoOperations.class);
        // refMethod.put("boundHashOps",BoundHashOperations.class);
        // refMethod.put("boundListOps",BoundListOperations.class);
        // refMethod.put("boundSetOps",BoundSetOperations.class);
        // refMethod.put("boundStreamOps",BoundStreamOperations.class);
        // refMethod.put("boundValueOps",BoundValueOperations.class);
        // refMethod.put("boundZSetOps",BoundZSetOperations.class);

        return new ProxyMetaDataMap(RedisTemplate.class, basicMethodName, refMethod);
    }

    @Override
    public boolean beforeParamHandler(String methodName, String[] parameterNames, Object[] args) {
        if ("rename".equalsIgnoreCase(methodName) || "renameIfAbsent".equalsIgnoreCase(methodName)) {
            renameHandle(parameterNames, args);
            return false;
        }

        if ("rightPopAndLeftPush".equals(methodName) || "rightPopAndLeftPush".equalsIgnoreCase(methodName)) {
            popPushHandle(parameterNames, args);
            return false;
        }

        return true;
    }

    @Override
    public void afterParamHandler(String methodName, String[] parameterNames, Object[] args) {
    }


    @Override
    public void paramHandler(Object[] args, int currentParamIndex, String currentParamName, Object currentParamValue) {
        if (KEY.equalsIgnoreCase(currentParamName) || currentParamName.endsWith(KEY_START_WITH_CAPITAL)) {
            final String resolveTenantKey = tenantCacheKeyHelper.resolveTenantKey(String.valueOf(args[0]));
            log.debug("single key {} > {}", args[0], resolveTenantKey);
            args[currentParamIndex] = resolveTenantKey;
            return;
        }


        if (KEYS.equalsIgnoreCase(currentParamName) || currentParamName.endsWith(KEYS_START_WITH_CAPITAL)) {
            keysHandle(args, currentParamIndex, currentParamValue);
        }

        if (MAP.equals(currentParamName) || MAP_KEY_ALIAS1.equals(currentParamName)) {
            if (currentParamValue instanceof Map) {
                final Map<String, Object> firstArgumentMap = (Map<String, Object>) currentParamValue;
                final HashMap<String, Object> cloneMap = new HashMap<>();
                for (Map.Entry<String, Object> entry : firstArgumentMap.entrySet()) {
                    cloneMap.put(tenantCacheKeyHelper.resolveTenantKey(entry.getKey()), entry.getValue());
                    log.debug("{} > {}", entry.getKey(), tenantCacheKeyHelper.resolveTenantKey(entry.getKey()));
                }
                args[currentParamIndex] = cloneMap;
            }
        }
    }


    private void keysHandle(Object[] args, int currentParamIndex, Object currentParamValue) {
        if (currentParamValue instanceof Collection) {
            final Collection<String> stringCollection = (Collection<String>) currentParamValue;

            List<String> paramList = new ArrayList<>();
            for (String s : stringCollection) {
                final String enhanceKey = tenantCacheKeyHelper.resolveTenantKey(s);
                paramList.add(enhanceKey);
                log.debug("{} > {}", s, enhanceKey);
            }
            args[currentParamIndex] = paramList;
        } else if (currentParamValue.getClass().isArray()) {
            final String[] paramValueArray = (String[]) currentParamValue;
            List<String> resolveList = new ArrayList<>();
            for (String v : paramValueArray) {
                resolveList.add(tenantCacheKeyHelper.resolveTenantKey(v));
            }
            args[currentParamIndex] = resolveList.toArray();
        } else {
            log.warn("Unsupport enhance class type  {}", args.getClass().getName());
        }
    }

    private void popPushHandle(String[] parameterNames, Object[] arguments) {
        if (parameterNames.length < 2) {
            return;
        }

        final String sourceKey = parameterNames[0];
        final String destinationKey = parameterNames[1];

        arguments[0] = tenantCacheKeyHelper.resolveTenantKey(String.valueOf(sourceKey));
        arguments[1] = tenantCacheKeyHelper.resolveTenantKey(String.valueOf(destinationKey));
    }


    private void renameHandle(String[] parameterNames, Object[] arguments) {
        if (parameterNames.length < 2) {
            return;
        }


        final String oldKey = parameterNames[0];
        final String newKey = parameterNames[1];

        arguments[0] = tenantCacheKeyHelper.resolveTenantKey(String.valueOf(oldKey));
        arguments[1] = tenantCacheKeyHelper.resolveTenantKey(String.valueOf(newKey));
    }


}
