package com.suny.tenant.mode.multiple.cache.springcache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.cache.Cache;

/**
 * 切入点 CacheAspectSupport
 * @author sunjianrong
 * @date 2021-05-14 17:14
 */
// @Component
// @Aspect
public class SpringCacheKeyAop implements AopInfrastructureBean {

    @Pointcut("execution(* org.springframework.cache.CacheManager.getCache(..)) ")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object Around(ProceedingJoinPoint pjp) throws Throwable {
        final Cache cache = (Cache) pjp.proceed();
        return cache;
    }
}
