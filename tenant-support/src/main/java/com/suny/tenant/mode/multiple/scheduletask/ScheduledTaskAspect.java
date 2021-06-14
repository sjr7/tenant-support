package com.suny.tenant.mode.multiple.scheduletask;

import com.suny.tenant.annotations.scheduletask.TenantScheduledTask;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author sunjianrong
 * @date 2021-05-07 11:17
 */
@Aspect
@Component
@Slf4j
public class ScheduledTaskAspect {
    private final ScheduledTaskWrapperExecutor scheduledTaskWrapperExecutor;

    public ScheduledTaskAspect(ScheduledTaskWrapperExecutor scheduledTaskWrapperExecutor) {
        this.scheduledTaskWrapperExecutor = scheduledTaskWrapperExecutor;
    }


    @Pointcut("@annotation(annotation)")
    public void execute(TenantScheduledTask annotation) {
    }


    @Around(value = "execute(annotation)", argNames = "proceedingJoinPoint,annotation")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, TenantScheduledTask annotation) throws Throwable {
        scheduledTaskWrapperExecutor.execute(o -> {
            try {
                proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                log.warn("scheduled task execute exception ! ", throwable);
            }
        });

        return null;
    }
}
