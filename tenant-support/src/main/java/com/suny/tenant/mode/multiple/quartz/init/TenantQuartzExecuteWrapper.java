package com.suny.tenant.mode.multiple.quartz.init;

import com.suny.tenant.mode.multiple.provider.SystemTenantHelper;
import com.suny.tenant.system.tenant.SysTenant;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author sunjianrong
 * @date 2021-05-10 11:14
 */
@Component
@Slf4j
public class TenantQuartzExecuteWrapper {

    @Autowired
    private SystemTenantHelper systemTenantHelper;


    /**
     * 执行包裹业务
     *
     * @param consumer 业务执行
     */
    public void wrapperExecute(Consumer<String> consumer) {
        final List<SysTenant> sysTenants = systemTenantHelper.getAll();
        for (SysTenant sysTenant : sysTenants) {
            try {
                final String tenantId = sysTenant.getTenantId();
                UserContext.setTenantId(tenantId);
                consumer.accept(tenantId);
            } finally {
                UserContext.clear();
            }

        }
    }

}
