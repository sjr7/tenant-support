package com.suny.tenant.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sunjianrong
 * @date 2021-05-18 11:16
 */
@Component
@Slf4j
public class TenantApplicationContext implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;

    public static <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        return APPLICATION_CONTEXT.getBeansOfType(requiredType);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }


    public static <T> T getBean(Class<T> requiredType) {
        return APPLICATION_CONTEXT.getBean(requiredType);
    }
}
