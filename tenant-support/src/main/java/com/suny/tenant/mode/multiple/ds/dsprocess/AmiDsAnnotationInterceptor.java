package com.suny.tenant.mode.multiple.ds.dsprocess;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.support.DataSourceClassResolver;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.suny.tenant.exception.DatasourceNotExistException;
import com.suny.tenant.mode.multiple.ds.utils.DatasourceKeyUtil;
import org.aopalliance.intercept.MethodInvocation;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 拦截处理
 *
 * @author sunjianrong
 * @date 2021-04-22 17:17
 */
public class AmiDsAnnotationInterceptor extends DynamicDataSourceAnnotationInterceptor {

    private final DataSourceClassResolver RESOLVER;
    private final DsProcessor dsProcessor;
    private final DynamicRoutingDataSource dataSource;
    private final DataSourceClassResolver dataSourceClassResolver;


    public AmiDsAnnotationInterceptor(DynamicRoutingDataSource dataSource, Boolean allowedPublicOnly, DsProcessor dsProcessor) {
        super(allowedPublicOnly, dsProcessor);
        dataSourceClassResolver = new DataSourceClassResolver(allowedPublicOnly);
        this.RESOLVER = dataSourceClassResolver;
        this.dsProcessor = dsProcessor;
        this.dataSource = dataSource;
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result;
        try {
            final String ds = this.determineDatasource(invocation);
            final Map<String, DataSource> currentDataSources = this.dataSource.getCurrentDataSources();
            boolean exist = currentDataSources.keySet().stream().anyMatch(key -> key.equals(ds));

            if (!exist) {
                throw new DatasourceNotExistException(DatasourceKeyUtil.parseTenantId(ds), DatasourceKeyUtil.parseDbKey(ds));
            }
            DynamicDataSourceContextHolder.push(ds);
            result = invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }

        return result;
    }

    private String determineDatasource(MethodInvocation invocation) {
        String key = RESOLVER.findDSKey(invocation.getMethod(), invocation.getThis());
        // return !key.isEmpty() && key.startsWith(DataSourceConstant.DYNAMIC_PREFIX) ? this.dsProcessor.determineDatasource(invocation, key) : key;
        return !key.isEmpty() ? this.dsProcessor.determineDatasource(invocation, key) : key;
    }

}
