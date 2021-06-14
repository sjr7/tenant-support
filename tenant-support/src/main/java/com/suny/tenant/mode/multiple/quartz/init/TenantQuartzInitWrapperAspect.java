package com.suny.tenant.mode.multiple.quartz.init;

import com.suny.tenant.annotations.quartz.TenantQuartzInitWrapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionException;

/**
 * setOrder(Ordered.LOWEST_PRECEDENCE - 3);
 * 故应该小于该值
 *
 * @author sunjianrong
 * @date 2021-05-10 11:12
 */
// @Order(Ordered.HIGHEST_PRECEDENCE+1)
@Aspect
@Component
@Slf4j
public class TenantQuartzInitWrapperAspect {

    @Autowired
    private TenantQuartzExecuteWrapper tenantQuartzExecuteWrapper;

    @Pointcut("@annotation(annotation)")
    public void execute(TenantQuartzInitWrapper annotation) {
    }


    @Around(value = "execute(annotation)", argNames = "proceedingJoinPoint,annotation")
    public void doAround(ProceedingJoinPoint proceedingJoinPoint, TenantQuartzInitWrapper annotation) throws Throwable {
        tenantQuartzExecuteWrapper.wrapperExecute(tenantId -> {
            try {
                log.debug("tenant id: {} execute quartz task !", tenantId);
                proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                log.warn("execute exception ! ", throwable);
                throw new CompletionException(throwable);
            }
        });

    }

}
