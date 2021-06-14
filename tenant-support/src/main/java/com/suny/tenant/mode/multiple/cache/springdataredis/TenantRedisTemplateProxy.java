package com.suny.tenant.mode.multiple.cache.springdataredis;

import com.suny.tenant.mode.multiple.helper.ProxyMethodMap;
import com.suny.tenant.mode.multiple.helper.TenantCacheKeyHelper;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.data.redis.core.*;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author sunjianrong
 * @date 2021-05-19 15:26
 */
// @ConditionalOnClass(RedisOperations.class)
// @Configuration
@Slf4j
public class TenantRedisTemplateProxy  {

    private static final List<String> PROXY_RT_BASIC_METHOD_LIST = new ArrayList<>();
    private static final List<String> PROXY_RT_REF_METHOD_LIST = new ArrayList<>();

    private final ProxyMethodMap proxyMethodMap = new ProxyMethodMap();

    private static final String KEY = "key";
    private static final String KEY_START_WITH_CAPITAL = "Key";
    private static final String KEYS = "keys";
    private static final String KEYS_START_WITH_CAPITAL = "Keys";
    private static final String MAP = "map";
    private static final String MAP_KEY_ALIAS1 = "m";

    @Autowired
    private TenantCacheKeyHelper tenantCacheKeyHelper;

    public TenantRedisTemplateProxy() {
        // proxyMethodMap.addSupportProxyInterface(ValueOperations.class);
        // proxyMethodMap.addSupportProxyInterface(ListOperations.class);
        // proxyMethodMap.addSupportProxyInterface(SetOperations.class);
        // proxyMethodMap.addSupportProxyInterface(StreamOperations.class);
        // proxyMethodMap.addSupportProxyInterface(ZSetOperations.class);
        // proxyMethodMap.addSupportProxyInterface(GeoOperations.class);
        // proxyMethodMap.addSupportProxyInterface(HyperLogLogOperations.class);
        // proxyMethodMap.addProxyInterface(ClusterOperations.class);
    }

    static {
        // PROXY_RT_BASIC_METHOD_LIST.add("hasKey");
        // PROXY_RT_BASIC_METHOD_LIST.add("countExistingKeys");
        // PROXY_RT_BASIC_METHOD_LIST.add("delete");
        // PROXY_RT_BASIC_METHOD_LIST.add("unlink");
        // PROXY_RT_BASIC_METHOD_LIST.add("type");
        // PROXY_RT_BASIC_METHOD_LIST.add("keys");
        // PROXY_RT_BASIC_METHOD_LIST.add("rename");
        // PROXY_RT_BASIC_METHOD_LIST.add("renameIfAbsent");
        // PROXY_RT_BASIC_METHOD_LIST.add("expire");
        // PROXY_RT_BASIC_METHOD_LIST.add("expireAt");
        // PROXY_RT_BASIC_METHOD_LIST.add("persist");
        // PROXY_RT_BASIC_METHOD_LIST.add("move");
        // PROXY_RT_BASIC_METHOD_LIST.add("dump");
        // PROXY_RT_BASIC_METHOD_LIST.add("restore");
        // PROXY_RT_BASIC_METHOD_LIST.add("getExpire");
        // PROXY_RT_BASIC_METHOD_LIST.add("sort");        // 不支持
        // PROXY_RT_BASIC_METHOD_LIST.add("watch");

        //特殊处理
        // PROXY_RT_BASIC_METHOD_LIST.add("boundGeoOps");
        // PROXY_RT_BASIC_METHOD_LIST.add("boundHashOps");
        // PROXY_RT_BASIC_METHOD_LIST.add("boundListOps");
        // PROXY_RT_BASIC_METHOD_LIST.add("boundSetOps");
        // PROXY_RT_BASIC_METHOD_LIST.add("opsForStream");
        // PROXY_RT_BASIC_METHOD_LIST.add("boundValueOps");
        // PROXY_RT_BASIC_METHOD_LIST.add("boundZSetOps");

        // PROXY_RT_REF_METHOD_LIST.add("opsForValue");
        // PROXY_RT_REF_METHOD_LIST.add("opsForList");
        // PROXY_RT_REF_METHOD_LIST.add("opsForSet");
        // PROXY_RT_REF_METHOD_LIST.add("opsForStream");
        // PROXY_RT_REF_METHOD_LIST.add("opsForZSet");
        // PROXY_RT_REF_METHOD_LIST.add("opsForGeo");
        // PROXY_RT_REF_METHOD_LIST.add("opsForHyperLogLog");
        // PROXY_RT_REF_METHOD_LIST.add("opsForCluster");

    }


    private static boolean isProxyRef(String methodName) {
        return PROXY_RT_REF_METHOD_LIST.stream().anyMatch(n -> n.equalsIgnoreCase(methodName));
    }

    private static boolean isProxyRedisTemplateBasicMethod(String methodName) {
        return PROXY_RT_BASIC_METHOD_LIST.stream().anyMatch(n -> n.equalsIgnoreCase(methodName));
    }


    // @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    // @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RedisTemplate) {
            log.debug("capture RedisTemplate bean {}", beanName);
            return getProxy(bean);
        }

        return bean;
    }


    public Object getProxy(Object obj) {

        ProxyFactory proxy = new ProxyFactory(obj);
        proxy.setProxyTargetClass(true);

        proxy.addAdvice((MethodInterceptor) invocation -> {
            final Method method = invocation.getMethod();
            final Object[] args = invocation.getArguments();
            final Object target = invocation.getThis();
            assert target != null;

            final String methodName = method.getName();

            final Object proceed = invocation.proceed();
            if (isProxyRef(methodName)) {
                log.debug("{} 触发引用调用增强", methodName);
                return getProxy(proceed);
            }


            if (isProxyRedisTemplateBasicMethod(methodName)) {
                log.debug("{} 触发方法调用增强", methodName);
                interceptor(method, args, target);
            }

            // if (proxyMethodMap.isProxyMethod(target.getClass(), methodName)) {
            //     interceptor(method, args, target);
            // }


            return proceed;

        });


        return proxy.getProxy();
    }


    private void interceptor(Method method, Object[] args, @Nullable Object target) throws Throwable {

        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        if (parameterNames == null || parameterNames.length == 0) {
            return;
        }

        final String methodName = method.getName();
        if ("rename".equalsIgnoreCase(methodName) || "renameIfAbsent".equalsIgnoreCase(methodName)) {
            renameHandle(parameterNames, args);
            return;
        }

        if ("rightPopAndLeftPush".equals(methodName) || "rightPopAndLeftPush".equalsIgnoreCase(methodName)) {
            popPushHandle(parameterNames, args);
            return;
        }


        for (int i = 0; i < parameterNames.length; i++) {
            String currentParamName = parameterNames[i];
            Object currentParamValue = args[i];
            proxyHandle(args, i, currentParamName, currentParamValue);
        }

    }

    private void proxyHandle(Object[] args, int currentParamIndex, String currentParamName, Object currentParamValue) {
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
