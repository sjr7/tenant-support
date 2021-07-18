package com.suny.tenant.mode.multiple.ds.config;

import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.mode.multiple.ds.dsprocess.AmiYmlDynamicDataSourceProvider;
import com.suny.tenant.mode.multiple.ds.router.AmiDynamicRoutingDataSource;
import com.suny.tenant.mode.multiple.ds.utils.DatasourceKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sunjianrong
 * @date 2021-04-28 10:37
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
public class AmiDsAutoConfiguration {
    // @Resource
    // private ILoginInfoContext loginInfoContext;

    @Resource
    private DynamicDataSourceProperties properties;

    @Resource
    private TenantProperties tenantProperties;

    // @Autowired
    // private SysDbSourceService sysDbSourceService;

    private final boolean appendPlatformTenantId = true;

    // @DependsOn("dataSource")
    // @Bean
    // @ConditionalOnMissingBean
    // public DataSourceProviderService dataSourceProviderService(){
    //     return new DefaultDataSourceProviderServiceImpl(sysDbSourceService);
    // }

    @Bean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        final String platformTenantId = tenantProperties.getDefaultTenantId();
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();

        Map<String, DataSourceProperty> replaceDatasourceMap = new LinkedHashMap<>();

        for (Map.Entry<String, DataSourceProperty> entry : datasourceMap.entrySet()) {
            final String newKey = DatasourceKeyUtil.genKey(platformTenantId, entry.getKey());
            replaceDatasourceMap.put(newKey, entry.getValue());
            log.debug("{} >>>>>> {}", entry.getKey(), newKey);
        }

        return new AmiYmlDynamicDataSourceProvider(replaceDatasourceMap);
    }

    @Bean
    // @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        checkDataSourceParam();
        AmiDynamicRoutingDataSource dataSource = new AmiDynamicRoutingDataSource(tenantProperties);
        // dataSource.setLoginInfoContext(loginInfoContext);
        if (appendPlatformTenantId) {
            final String defaultDsName = getDefaultDsName();
            dataSource.setPrimaryDb(properties.getPrimary());
            dataSource.setPrimary(defaultDsName);
        } else {
            final String primary = properties.getPrimary();
            dataSource.setPrimaryDb(primary);
            dataSource.setPrimary(primary);
        }
        dataSource.setStrict(properties.getStrict());
        dataSource.setStrategy(properties.getStrategy());
        dataSource.setProvider(dynamicDataSourceProvider);
        dataSource.setP6spy(properties.getP6spy());
        dataSource.setSeata(properties.getSeata());
        return dataSource;
    }

    private String getDefaultDsName() {
        final String platformTenantId = tenantProperties.getDefaultTenantId();
        return DatasourceKeyUtil.genKey(platformTenantId, properties.getPrimary());
    }

    private void checkDataSourceParam() {
        if (tenantProperties == null || tenantProperties.getDefaultTenantId() == null) {
            throw new IllegalArgumentException("Property 'tenant.platform-tenant-id'is required");
        }
    }
}
