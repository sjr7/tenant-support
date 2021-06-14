package com.suny.tenant.mode.multiple.ds.router;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.suny.tenant.api.auth.ILoginInfoContext;
import com.suny.tenant.exception.ThreadTenantIdNotFoundException;
import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.mode.multiple.ds.utils.DatasourceKeyUtil;
import com.suny.tenant.mode.multiple.helper.EnvironmentHelper;
import com.suny.tenant.thread.UserContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;

/**
 * @author sunjianrong
 * @date 2021-04-28 13:51
 */
@Slf4j
public class AmiDynamicRoutingDataSource extends DynamicRoutingDataSource {

    @Setter
    @Getter
    private String primaryDb;

    @Setter
    @Getter
    private ILoginInfoContext loginInfoContext;

    private final TenantProperties properties;

    public AmiDynamicRoutingDataSource(TenantProperties properties) {
        this.properties = properties;
    }

    @Override
    public DataSource determineDataSource() {
        return super.determineDataSource();
    }

    @Override
    public DataSource getDataSource(String ds) {
        boolean hasDs = StringUtils.isNotBlank(ds);

        if (DatasourceKeyUtil.isFinalDsKey(ds)) {
            return super.getDataSource(ds);
        }

        if (EnvironmentHelper.webReuqest()) {
            // final String tenantId = loginInfoContext.getTenantId();
            final String tenantId = UserContext.getTenantId();
            if (StringUtils.isBlank(tenantId)) {
                throw new ThreadTenantIdNotFoundException();
                // throw new IllegalArgumentException("Cann't get tenantId from web request");
            }


            if (hasDs) {
                if (ds.contains(tenantId)) {
                    return super.getDataSource(ds);
                }

                return super.getDataSource(DatasourceKeyUtil.genKey(tenantId, ds));
            } else {
                return super.getDataSource(DatasourceKeyUtil.genKey(tenantId, primaryDb));
            }
        }


        final String tenantId = UserContext.getTenantId();
        if (StringUtils.isNotBlank(tenantId)) {
            if (hasDs) {
                return super.getDataSource(DatasourceKeyUtil.genKey(tenantId, ds));
            } else {
                return super.getDataSource(DatasourceKeyUtil.genKey(tenantId, primaryDb));
            }
        } else {
            // 兜底策略
            final String threadName = Thread.currentThread().getName();
            if (threadName.contains("quartzScheduler_Worker")) {
                if (hasDs) {
                    return super.getDataSource(DatasourceKeyUtil.genKey(properties.getDefaultTenantId(), ds));
                } else {
                    return super.getDataSource(DatasourceKeyUtil.genKey(properties.getDefaultTenantId(), primaryDb));
                }
            }

            throw new ThreadTenantIdNotFoundException();
            // throw new IllegalArgumentException("Cann't get current thread tenantId");
        }

    }


    private DataSource get(String ds) {
        return super.getDataSource(ds);
    }
}
