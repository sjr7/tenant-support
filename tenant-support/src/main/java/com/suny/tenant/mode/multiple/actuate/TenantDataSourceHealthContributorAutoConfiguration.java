package com.suny.tenant.mode.multiple.actuate;

import com.suny.tenant.mode.multiple.TenantProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.health.CompositeHealthContributorConfiguration;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunjianrong
 * @date 2021-06-02 16:37
 */

// @Configuration(
//         proxyBeanMethods = false
// )
// @ConditionalOnClass({JdbcTemplate.class, AbstractRoutingDataSource.class})
// @ConditionalOnBean({DataSource.class})
// @ConditionalOnEnabledHealthIndicator("db")
// @AutoConfigureAfter({DataSourceAutoConfiguration.class})
class TenantDataSourceHealthContributorAutoConfiguration extends CompositeHealthContributorConfiguration<AbstractHealthIndicator, DataSource> implements InitializingBean {

    // @Autowired
    private TenantProperties tenantProperties;


    private final Collection<DataSourcePoolMetadataProvider> metadataProviders;
    private DataSourcePoolMetadataProvider poolMetadataProvider;

    public TenantDataSourceHealthContributorAutoConfiguration(Map<String, DataSource> dataSources, ObjectProvider<DataSourcePoolMetadataProvider> metadataProviders) {
        this.metadataProviders = (Collection) metadataProviders.orderedStream().collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // this.poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(this.metadataProviders);
    }

    // @Primary
    // @Bean
    // @ConditionalOnMissingBean(
    //         name = {"dbHealthIndicator", "dbHealthContributor"}
    // )
    public HealthContributor dbHealthContributor(Map<String, DataSource> dataSources) {
        return this.createContributor(dataSources);
    }

    @Override
    protected AbstractHealthIndicator createIndicator(DataSource source) {
        if (source instanceof AbstractRoutingDataSource) {
            return new RoutingDataSourceHealthIndicator();
        }
        return new TenantDataSourceHealthIndicator(source, this.getValidationQuery(source), tenantProperties);
    }

    private String getValidationQuery(DataSource source) {
        DataSourcePoolMetadata poolMetadata = this.poolMetadataProvider.getDataSourcePoolMetadata(source);
        return poolMetadata != null ? poolMetadata.getValidationQuery() : null;
    }

    static class RoutingDataSourceHealthIndicator extends AbstractHealthIndicator {
        RoutingDataSourceHealthIndicator() {
        }

        @Override
        protected void doHealthCheck(Builder builder) throws Exception {
            builder.unknown().withDetail("routing", true);
        }
    }
}