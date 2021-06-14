package com.suny.tenant.mode.multiple.proxy;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author sunjianrong
 * @date 2021-05-24 14:13
 */
@Slf4j
public class ProxyMetaDataMap {

    private final Class<?> proxyClass;
    private final Set<String> baseMethodName;
    private final Map<String, Class<?>> refMethodNameMapping;
    private final InnerProxyMethodMap innerProxyMethodMap;


    public ProxyMetaDataMap(Class<?> proxyClass, Map<String, Class<?>> refMethodNameMapping) {
        this(proxyClass, parseMethods(proxyClass), refMethodNameMapping);
    }

    public ProxyMetaDataMap(Class<?> proxyClass, Set<String> baseProxyMethodName, Map<String, Class<?>> refProxyMethodMapping) {
        assert proxyClass != null;
        this.proxyClass = proxyClass;
        if (null == baseProxyMethodName) {
            baseProxyMethodName = new HashSet<>();
        }
        if (null == refProxyMethodMapping) {
            refProxyMethodMapping = new HashMap<>();
        }
        this.baseMethodName = baseProxyMethodName;
        this.refMethodNameMapping = refProxyMethodMapping;

        final InnerProxyMethodMap innerProxyMethodMap = new InnerProxyMethodMap();
        for (Map.Entry<String, Class<?>> entry : refProxyMethodMapping.entrySet()) {
            if (baseMethodName.contains(entry.getKey())) {
                baseProxyMethodName.remove(entry.getKey());
            }
            innerProxyMethodMap.addSupportProxyInterface(entry.getValue());
        }
        this.innerProxyMethodMap = innerProxyMethodMap;
    }


    public Class<?> getProxyClass(){
        return this.proxyClass;
    }

    public boolean isProxyBaseMethod(Class<?> targetClass, String methodName) {
        if (!proxyClass.isAssignableFrom(targetClass)) {
            return false;
        }
        return baseMethodName.stream().anyMatch(m -> m.equals(methodName));
    }


    public boolean isGetRefInstance(Class<?> targetClass, String methodName) {
        if (!proxyClass.isAssignableFrom(targetClass)) {
            return false;
        }

        return refMethodNameMapping.keySet().stream().anyMatch(m -> m.equals(methodName));
    }


    public boolean isProxyRefMethod(Class<?> targetClass, String methodName) {
        return innerProxyMethodMap.isProxyMethod(targetClass, methodName);
    }

    public void excludeMethod(Class<?> clazz, String methodName) {
        innerProxyMethodMap.excludeMethod(clazz, methodName);
    }


    private static class InnerProxyMethodMap {
        private final Map<String, Set<String>> methodMap = new HashMap<>();
        private final Set<Class<?>> interfaceSet = new HashSet<>();

        public void addSupportProxyInterface(Class<?> clazz) {
            interfaceSet.add(clazz);
            methodMap.put(clazz.getName(), parseMethods(clazz));
        }


        public void excludeMethod(Class<?> clazz, String methodName) {
            for (Class<?> c : interfaceSet) {
                if (c.isAssignableFrom(clazz)) {
                    final Set<String> methodSet = methodMap.get(c.getName());
                    methodSet.remove(methodName);
                    break;
                }
            }
        }

        public boolean isProxyMethod(Class<?> currentClass, String methodName) {
            String targetInterface = null;
            for (Class<?> c : interfaceSet) {
                if (c.isAssignableFrom(currentClass)) {
                    targetInterface = c.getName();
                    break;
                }
            }
            if (StringUtils.isBlank(targetInterface)) {
                // log.warn("Not support proxy {}", currentClass.getName());
                return false;
            }

            for (Map.Entry<String, Set<String>> opsEntry : methodMap.entrySet()) {
                if (opsEntry.getKey().equals(targetInterface)) {
                    final Set<String> methodList = opsEntry.getValue();
                    for (String m : methodList) {
                        if (m.equals(methodName)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }


    private static Set<String> parseMethods(Class<?> clazz) {
        Set<String> methodList = new HashSet<>();
        Arrays.stream(clazz.getDeclaredMethods()).forEach(m -> {
            methodList.add(m.getName());
            log.debug("Support method {} > {}", clazz.getName(), m.getName());
        });
        return methodList;
    }


}
