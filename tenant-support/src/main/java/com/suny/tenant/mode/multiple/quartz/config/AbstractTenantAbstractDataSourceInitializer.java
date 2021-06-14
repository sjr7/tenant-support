package com.suny.tenant.mode.multiple.quartz.config;

import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.thread.UserContext;
import org.springframework.boot.jdbc.AbstractDataSourceInitializer;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author sunjianrong
 * @date 2021-05-29 11:16
 */
public abstract class AbstractTenantAbstractDataSourceInitializer extends AbstractDataSourceInitializer {

    private static final String PLATFORM_PLACEHOLDER = "@@platform@@";

    private final DataSource dataSource;

    private final ResourceLoader resourceLoader;

    private final TenantProperties dsProperties;


    protected AbstractTenantAbstractDataSourceInitializer(DataSource dataSource, ResourceLoader resourceLoader, TenantProperties dsProperties) {
        super(dataSource, resourceLoader);
        this.dsProperties = dsProperties;
        Assert.notNull(dataSource, "DataSource must not be null");
        Assert.notNull(resourceLoader, "ResourceLoader must not be null");
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
    }


    @PostConstruct
    @Override
    protected void initialize() {
        UserContext.setTenantId(dsProperties.getDefaultTenantId());
        if (!isEnabled()) {
            return;
        }

        try {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            String schemaLocation = getSchemaLocation();
            if (schemaLocation.contains(PLATFORM_PLACEHOLDER)) {
                String platform = getDatabaseName();
                schemaLocation = schemaLocation.replace(PLATFORM_PLACEHOLDER, platform);
            }
            populator.addScript(this.resourceLoader.getResource(schemaLocation));
            populator.setContinueOnError(true);
            customize(populator);
            DatabasePopulatorUtils.execute(populator, this.dataSource);
        } finally {
            UserContext.clear();
        }


    }

    private boolean isEnabled() {
        if (getMode() == DataSourceInitializationMode.NEVER) {
            return false;
        }
        if (getMode() == DataSourceInitializationMode.EMBEDDED
                && !EmbeddedDatabaseConnection.isEmbedded(this.dataSource)) {
            return false;
        }
        return true;
    }
}
