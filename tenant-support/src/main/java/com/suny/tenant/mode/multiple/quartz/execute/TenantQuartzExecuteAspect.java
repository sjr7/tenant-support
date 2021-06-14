package com.suny.tenant.mode.multiple.quartz.execute;

import com.suny.tenant.annotations.quartz.TenantQuartzExecute;
import com.suny.tenant.constant.TenantSupportConstant;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author sunjianrong
 * @date 2021-06-01 14:13
 */
@Aspect
@Component
@Slf4j
public class TenantQuartzExecuteAspect {

    @Pointcut("@annotation(annotation)")
    public void execute(TenantQuartzExecute annotation) {
    }

    @Around(value = "execute(annotation)", argNames = "proceedingJoinPoint,annotation")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, TenantQuartzExecute annotation) throws Throwable {
        // todo 解决 quartz 线程被主线程污染
        UserContext.clear();

        final Object[] args = proceedingJoinPoint.getArgs();
        if (args == null || args.length < 1) {
            return proceedingJoinPoint.proceed();
        }

        final Object firstArg = args[0];
        if (!(firstArg instanceof JobExecutionContext)) {
            throw new IllegalArgumentException("The first argument must be JobExecutionContext");
        }


        final JobExecutionContext jobExecutionContext = (JobExecutionContext) firstArg;
        final JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
        final String tenantId = mergedJobDataMap.getString(TenantSupportConstant.REQUEST_HEADER_TENANT_ID);
        if (StringUtils.isBlank(tenantId)) {
            throw new IllegalArgumentException("Could not find parameter [" + TenantSupportConstant.REQUEST_HEADER_TENANT_ID + "] in JobDataMap");
        }

        try {
            UserContext.setTenantId(tenantId);
            log.debug("tenant id: {} execute quartz task !", tenantId);
            return proceedingJoinPoint.proceed();
        } finally {
            UserContext.clear();
        }


    }
}
