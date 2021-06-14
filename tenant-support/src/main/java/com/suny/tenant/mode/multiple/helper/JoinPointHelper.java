package com.suny.tenant.mode.multiple.helper;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author sunjianrong
 * @date 2021-05-28 10:34
 */
public final class JoinPointHelper {

    public static String getClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getName();
    }

    public static String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    public static Method getMethod(JoinPoint joinPoint) throws NoSuchMethodException {
        final String methodName = getMethodName(joinPoint);
        Class<?> classTarget = joinPoint.getTarget().getClass();
        Class<?>[] par = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        return classTarget.getMethod(methodName, par);
    }


}
