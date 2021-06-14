package com.suny.tenant.mode.multiple.asynctask;

import com.suny.tenant.annotations.asynctask.TenantControllerAsync;
import com.suny.tenant.api.auth.ILoginInfoContext;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionException;

/**
 * @author sunjianrong
 * @date 2021-05-08 14:03
 */
@Aspect
@Component
@Slf4j
public class ControllerCallAsyncFunctionAspect {

    private final ILoginInfoContext loginInfoContext;

    public ControllerCallAsyncFunctionAspect(ILoginInfoContext loginInfoContext) {
        this.loginInfoContext = loginInfoContext;
    }


    @Pointcut("@annotation(annotation)")
    public void execute(TenantControllerAsync annotation) {
    }


    @Around(value = "execute(annotation)", argNames = "proceedingJoinPoint,annotation")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, TenantControllerAsync annotation) {
        final String tenantId = loginInfoContext.getTenantId();

        try {
            UserContext.setTenantId(tenantId);
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throw new CompletionException(throwable);
            }
        } finally {
            UserContext.clear();
        }

    }

}
