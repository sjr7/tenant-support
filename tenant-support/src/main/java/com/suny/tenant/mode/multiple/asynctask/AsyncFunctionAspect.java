package com.suny.tenant.mode.multiple.asynctask;

import com.suny.tenant.annotations.asynctask.TenantAsync;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;

/**
 * 暂时无法调整 @Async 执行优先级,导致当前切面会在线程池中执行
 *
 * @author sunjianrong
 * @date 2021-05-07 16:02
 */
@Order(-2021)
@Aspect
@Component
@Slf4j
public class AsyncFunctionAspect {


    @Pointcut("@annotation(annotation)")
    public void execute(TenantAsync annotation) {
    }


    @Around(value = "execute(annotation)", argNames = "proceedingJoinPoint,annotation")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, TenantAsync annotation) {

        Object[] args = proceedingJoinPoint.getArgs();
        Class<?>[] params = new Class<?>[args.length];
        for (int i = 0; i < args.length; ++i) {
            params[i] = args[i].getClass();
        }

        Class<?> returnType;
        Method method;
        try {
            method = proceedingJoinPoint.getTarget().getClass().getMethod(proceedingJoinPoint.getSignature().getName(), params);
            returnType = method.getReturnType();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        final String tenantId = UserContext.getTenantId();

        // 参考 spring async 源码 AsyncExecutionAspectSupport

        if (CompletableFuture.class.isAssignableFrom(returnType)
                || ListenableFuture.class.isAssignableFrom(returnType)
                || Future.class.isAssignableFrom(returnType)) {
            return AsyncWrapperExecutor.submit(tenantId,
                    () -> {
                        try {
                            return (Future<?>) proceedingJoinPoint.proceed();
                        } catch (Throwable throwable) {
                            throw new CompletionException(throwable);
                        }
                    });
        } else {
            AsyncWrapperExecutor.execute(tenantId,
                    () -> {
                        try {
                            proceedingJoinPoint.proceed();
                        } catch (Throwable throwable) {
                            throw new CompletionException(throwable);
                        }
                    });
        }

        return null;

    }


}
