package com.suny.tenant.mode.multiple.proxy;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

/**
 * @author sunjianrong
 * @date 2021-05-24 19:12
 */
@Slf4j
public abstract class AbstractSimpleParamProxy implements BeanPostProcessor {

    private final ProxyMetaDataMap proxyMetaDataMap;


    public AbstractSimpleParamProxy() {
        final ProxyMetaDataMap initMap = initMetaDataMap();
        if (initMap == null) {
            throw new NullPointerException("proxyMetaDataMap is required!");
        }
        this.proxyMetaDataMap = initMap;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        final Class<?> proxyClass = proxyMetaDataMap.getProxyClass();
        if (proxyClass.isAssignableFrom(bean.getClass())) {
            log.debug("capture RedissonClient bean {}", beanName);
            return getProxy(bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
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

            if (proxyMetaDataMap.isProxyBaseMethod(target.getClass(), methodName)) {
                interceptor(method, args, target);
                return invocation.proceed();
            }


            if (proxyMetaDataMap.isGetRefInstance(target.getClass(), methodName)) {
                return getProxy(invocation.proceed());
            }

            if (proxyMetaDataMap.isProxyRefMethod(target.getClass(), methodName)) {
                interceptor(method, args, target);
                return invocation.proceed();
            }

            return invocation.proceed();

        });


        return proxy.getProxy();
    }

    private void interceptor(Method method, Object[] args, @Nullable Object target) throws Throwable {
        if (args == null || args.length == 0) {
            return;
        }

        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

        assert parameterNames != null;

        final boolean executeNext = beforeParamHandler(method.getName(), parameterNames, args);
        if (!executeNext) {
            return;
        }


        for (int i = 0; i < parameterNames.length; i++) {
            String currentParamName = parameterNames[i];
            Object currentParamValue = args[i];
            paramHandler(args, i, currentParamName, currentParamValue);
        }

        afterParamHandler(method.getName(), parameterNames, args);

    }


    public abstract ProxyMetaDataMap initMetaDataMap();


    public abstract boolean beforeParamHandler(String methodName, String[] parameterNames, Object[] args);

    public abstract void paramHandler(Object[] args, int currentParamIndex, String currentParamName, Object currentParamValue);

    public abstract void afterParamHandler(String methodName, String[] parameterNames, Object[] args);

}
