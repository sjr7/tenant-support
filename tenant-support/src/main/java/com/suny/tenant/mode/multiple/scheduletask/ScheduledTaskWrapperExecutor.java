package com.suny.tenant.mode.multiple.scheduletask;

import com.suny.tenant.mode.multiple.provider.SystemTenantHelper;
import com.suny.tenant.system.tenant.SysTenant;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author sunjianrong
 * @date 2021-05-07 11:12
 */
@Component
@Slf4j
public class ScheduledTaskWrapperExecutor {
    private final SystemTenantHelper systemTenantHelper;

    public ScheduledTaskWrapperExecutor(SystemTenantHelper systemTenantHelper) {
        this.systemTenantHelper = systemTenantHelper;
    }


    /**
     * 执行定时任务
     *
     * @param consumer 业务执行
     */
    public void execute(Consumer<Object> consumer) {
        final List<SysTenant> sysTenants = systemTenantHelper.getAll();
        for (SysTenant sysTenant : sysTenants) {
            try {
                UserContext.setTenantId(sysTenant.getTenantId());
                consumer.accept(sysTenant.getTenantId());
            } finally {
                UserContext.clear();
            }

        }
    }


    /**
     * 执行主库定时任务
     *
     * @param consumer 业务执行
     */
    public void onlyExecuteMaster(Consumer<Object> consumer) {
        final SysTenant sysTenant = systemTenantHelper.getSystemTenant();
        if (sysTenant == null) {
            return;
        }

        try {
            UserContext.setTenantId(sysTenant.getTenantId());
            consumer.accept(sysTenant.getTenantId());
        } finally {
            UserContext.clear();
        }
    }
}
