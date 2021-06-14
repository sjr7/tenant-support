package com.suny.tenant.mode.multiple.ds.notify;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.mode.multiple.ds.provider.DataSourceProviderService;
import com.suny.tenant.mode.multiple.ds.utils.DatasourceKeyUtil;
import com.suny.tenant.system.datasource.SysDataSource;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-25 10:45
 */
@Component
@Slf4j
public class DataSourceNotify {
    private final DataSourceProviderService dataSourceProviderService;
    private final TenantProperties properties;
    private final DataSource dataSource;
    private final DefaultDataSourceCreator dataSourceCreator;


    public DataSourceNotify(DataSource dataSource, DefaultDataSourceCreator defaultDataSourceCreator, TenantProperties properties, DataSourceProviderService dataSourceProviderService) {
        this.dataSource = dataSource;
        this.dataSourceCreator = defaultDataSourceCreator;
        this.properties = properties;
        this.dataSourceProviderService = dataSourceProviderService;

        try {
            log.debug("init datasource !");
            UserContext.setTenantId(properties.getDefaultTenantId());
            final List<SysDataSource> sysDataSources = dataSourceProviderService.loadAll();
            for (SysDataSource sysDataSource : sysDataSources) {
                add(sysDataSource);
            }
        } finally {
            UserContext.clear();
        }
    }

    /**
     * 添加数据源
     *
     * @param sysDataSource 数据源信息
     * @return 添加状态
     */
    public boolean add(SysDataSource sysDataSource) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;

        final DataSourceProperty dataSourceProperty = new DataSourceProperty();
        if ("pg".equalsIgnoreCase(sysDataSource.getDbType())) {
            dataSourceProperty.setDriverClassName("org.postgresql.Driver");
        }

        dataSourceProperty.setUrl(sysDataSource.getUrl());
        dataSourceProperty.setUsername(sysDataSource.getUsername());
        dataSourceProperty.setPassword(sysDataSource.getPassword());
        final String key = DatasourceKeyUtil.genKey(sysDataSource.getTenantId(), sysDataSource.getUsername());
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource(key, dataSource);

        log.debug("Load database dynamic data source {}", key);
        return true;
    }


    /**
     * 移除数据源
     *
     * @param datasourceKey 数据源key
     */
    public void remove(String datasourceKey) {
        log.debug("Remove dynamic data source {}", datasourceKey);
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(datasourceKey);
    }

    /**
     * 更新数据源信息
     *
     * @param sysDataSource 数据源信息
     */
    public void update(SysDataSource sysDataSource) {
        final String genKey = DatasourceKeyUtil.genKey(sysDataSource.getTenantId(), sysDataSource.getDatasourceName());
        log.debug("Update dynamic data source {}", genKey);
        remove(genKey);
        add(sysDataSource);
    }
}
