package com.suny.tenant.mode.multiple.common;

import com.suny.tenant.annotations.common.TenantCycleExecute;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sunjianrong
 * @date 2021-05-11 16:44
 */
@Aspect
@Component
@Slf4j
public class TenantWrapperAspect {

    private final TenantCycleExecuteWrapper tenantCycleExecuteWrapper;

    public TenantWrapperAspect(TenantCycleExecuteWrapper tenantCycleExecuteWrapper) {
        this.tenantCycleExecuteWrapper = tenantCycleExecuteWrapper;
    }


    @Pointcut("@annotation(annotation)")
    public void execute(TenantCycleExecute annotation) {
    }


    @Around(value = "execute(annotation)", argNames = "proceedingJoinPoint,annotation")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, TenantCycleExecute annotation) {
        Signature signature = proceedingJoinPoint.getSignature();
        Class<?> returnType = ((MethodSignature) signature).getReturnType();
        boolean isVoid = returnType == Void.TYPE;

        if (isVoid) {
            tenantCycleExecuteWrapper.execute(() -> {
                try {
                    proceedingJoinPoint.proceed();
                } catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            });
            return null;
        }

        final Map<String, Object> submitResultMap = tenantCycleExecuteWrapper.submit(() -> {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });


        if (returnType.isPrimitive()) {
            // Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE
            if (returnType == Integer.TYPE) {
                Integer intRes = 0;

                for (Object o : submitResultMap.values()) {
                    intRes += (Integer) o;
                }
                return intRes;
            }


            if (returnType == Long.TYPE) {
                Long longRes = 0L;

                for (Object o : submitResultMap.values()) {
                    longRes += (Long) o;
                }
                return longRes;
            }

        }

        return null;
    }
}
