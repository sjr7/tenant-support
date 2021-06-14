package com.suny.tenant.mode.multiple.ds.dsprocess;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.suny.tenant.api.auth.ILoginInfoContext;
import com.suny.tenant.exception.ThreadTenantIdNotFoundException;
import com.suny.tenant.mode.multiple.ds.utils.DatasourceKeyUtil;
import com.suny.tenant.mode.multiple.helper.EnvironmentHelper;
import com.suny.tenant.thread.UserContext;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据源处理器
 *
 * @author sunjianrong
 * @date 2021-04-22 15:43
 */
public class AmiDsProcessor extends DsProcessor {

    private final ILoginInfoContext loginInfoContext;

    public AmiDsProcessor(ILoginInfoContext loginInfoContext) {
        this.loginInfoContext = loginInfoContext;
    }

    @Override
    public boolean matches(String key) {
        // return key.startsWith(DataSourceConstant.DYNAMIC_PREFIX);
        return true;
    }

    @Override
    public String doDetermineDatasource(MethodInvocation invocation, String key) {
        // key = key.replace(DataSourceConstant.DYNAMIC_PREFIX, "");
        final String tenantId;
        if (EnvironmentHelper.webReuqest()) {
            // tenantId = loginInfoContext.getTenantId();
            tenantId = UserContext.getTenantId();
            if (StringUtils.isBlank(tenantId) || tenantId.contains("null")) {
                throw new ThreadTenantIdNotFoundException();
                // throw new RuntimeException("can't get tenant id from request !");
            }
        } else {
            tenantId = UserContext.getTenantId();
            if (StringUtils.isBlank(tenantId)) {
                throw new ThreadTenantIdNotFoundException();
                // throw new RuntimeException("can't get tenant id from thread context !");
            }
        }


        return DatasourceKeyUtil.genKey(tenantId, key);
    }
}
