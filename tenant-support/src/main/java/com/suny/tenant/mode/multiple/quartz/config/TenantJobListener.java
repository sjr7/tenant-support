package com.suny.tenant.mode.multiple.quartz.config;

import com.suny.tenant.constant.TenantSupportConstant;
import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.mode.multiple.quartz.debuglog.QuartzDebugLog;
import com.suny.tenant.mode.multiple.quartz.debuglog.QuartzDebugLogService;
import com.suny.tenant.thread.UserContext;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author sunjianrong
 * @date 2021-06-01 18:00
 */
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class,
        PlatformTransactionManager.class})
@Component
public class TenantJobListener implements JobListener {
    private final QuartzDebugLogService quartzDebugLogService;

    public TenantJobListener(QuartzDebugLogService quartzDebugLogService) {
        this.quartzDebugLogService = quartzDebugLogService;
    }

    @Autowired
    private TenantProperties tenantProperties;

    @Override
    public String getName() {
        return "TenantJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        UserContext.setTenantId(context.getJobDetail().getJobDataMap().getString(TenantSupportConstant.REQUEST_HEADER_TENANT_ID));
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        UserContext.clear();
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        try {
            if (tenantProperties.getQuartz().isEnableExecuteDebugLog()) {
                final JobDetail jobDetail = context.getJobDetail();
                final QuartzDebugLog quartzDebugLog = new QuartzDebugLog();
                quartzDebugLog.setJobKey(jobDetail.getKey().toString());
                quartzDebugLog.setJobClass(jobDetail.getJobClass().getName());
                final Object tenantIdObject = jobDetail.getJobDataMap().get(TenantSupportConstant.REQUEST_HEADER_TENANT_ID);
                String tenantId = null;
                if (tenantIdObject != null) {
                    tenantId = String.valueOf(tenantIdObject);
                }
                quartzDebugLog.setTenantId(tenantId);
                quartzDebugLog.setFiretime(context.getFireTime());
                quartzDebugLog.setScheduledFireTime(context.getScheduledFireTime());
                quartzDebugLogService.saveLog(quartzDebugLog);
            }
        } finally {
            UserContext.clear();
        }


    }
}
