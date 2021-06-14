package com.suny.tenant.mode.multiple.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author sunjianrong
 * @date 2021-05-24 9:48
 */
@Deprecated
@Slf4j
public class ProxyMethodMap {

    private final Map<String, Set<String>> methodMap = new HashMap<>();
    private final Set<Class<?>> interfaceSet = new HashSet<>();

    public void addSupportProxyInterface(Class<?> clazz) {
        interfaceSet.add(clazz);
        methodMap.put(clazz.getName(), parseMethodList(clazz));
    }


    private static Set<String> parseMethodList(Class<?> clazz) {
        Set<String> methodList = new HashSet<>();
        Arrays.stream(clazz.getDeclaredMethods()).forEach(m -> {
            methodList.add(m.getName());
            log.debug("Support method {} > {}", clazz.getName(), m.getName());
        });
        return methodList;
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
            log.warn("Not support proxy {}", currentClass.getName());
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
