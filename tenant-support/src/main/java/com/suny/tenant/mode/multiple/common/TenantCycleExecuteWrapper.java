package com.suny.tenant.mode.multiple.common;

import com.suny.tenant.mode.multiple.provider.SystemTenantHelper;
import com.suny.tenant.system.tenant.SysTenant;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunjianrong
 * @date 2021-05-11 16:12
 */
@Component
@Slf4j
public class TenantCycleExecuteWrapper {

    private final SystemTenantHelper systemTenantHelper;

    public TenantCycleExecuteWrapper(SystemTenantHelper systemTenantHelper) {
        this.systemTenantHelper = systemTenantHelper;
    }


    public <R> Map<String, Object> submit(NoneParamAndReturnExecute<R> execute) {
        Map<String, Object> submitResult = new HashMap<>();

        final List<SysTenant> sysTenants = systemTenantHelper.getAll();
        for (SysTenant sysTenant : sysTenants) {
            try {
                UserContext.setTenantId(sysTenant.getTenantId());
                final R r = execute.execute();
                submitResult.put(sysTenant.getTenantId(), r);
            } finally {
                UserContext.clear();
            }

        }

        return submitResult;
    }


    /**
     * 包裹执行业务
     *
     * @param execute 业务执行
     */
    public void execute(NoneParamAndVoidExecute execute) {
        final List<SysTenant> sysTenants = systemTenantHelper.getAll();
        for (SysTenant sysTenant : sysTenants) {
            try {
                UserContext.setTenantId(sysTenant.getTenantId());
                execute.execute();
            } finally {
                UserContext.clear();
            }

        }
    }
}
