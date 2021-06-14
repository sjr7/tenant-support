package com.suny.tenant.mode.multiple.actuate;

import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthContributorAutoConfiguration;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.jdbc.metadata.CompositeDataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author sunjianrong
 * @date 2021-06-02 15:53
 */
@Configuration
@Slf4j
public class TenantDataSourceHealthConfig extends DataSourceHealthContributorAutoConfiguration {

    private final TenantProperties tenantProperties;
    private final Collection<DataSourcePoolMetadataProvider> metadataProviders;
    private final DataSourcePoolMetadataProvider poolMetadataProvider;

    public TenantDataSourceHealthConfig(Map<String, DataSource> dataSources, ObjectProvider<DataSourcePoolMetadataProvider> metadataProviders, TenantProperties tenantProperties) {
        super(dataSources, metadataProviders);
        this.tenantProperties = tenantProperties;
        this.metadataProviders = (Collection) metadataProviders.orderedStream().collect(Collectors.toList());
        this.poolMetadataProvider = new CompositeDataSourcePoolMetadataProvider(this.metadataProviders);
    }

    @Override
    protected AbstractHealthIndicator createIndicator(DataSource source) {
        UserContext.setTenantId(tenantProperties.getDefaultTenantId());
        try {
            if (source instanceof AbstractRoutingDataSource) {
                return new TenantDataSourceHealthContributorAutoConfiguration.RoutingDataSourceHealthIndicator();
            }
            return new TenantDataSourceHealthIndicator(source, this.getValidationQuery(source), tenantProperties);
        } finally {
            UserContext.clear();
        }

    }


    private String getValidationQuery(DataSource source) {
        DataSourcePoolMetadata poolMetadata = this.poolMetadataProvider.getDataSourcePoolMetadata(source);
        return poolMetadata != null ? poolMetadata.getValidationQuery() : null;
    }

    static class RoutingDataSourceHealthIndicator extends AbstractHealthIndicator {
        RoutingDataSourceHealthIndicator() {
        }

        @Override
        protected void doHealthCheck(Health.Builder builder) throws Exception {
            builder.unknown().withDetail("routing", true);
        }
    }
}
