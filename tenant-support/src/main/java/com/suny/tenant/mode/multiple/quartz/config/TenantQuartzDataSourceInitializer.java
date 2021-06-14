package com.suny.tenant.mode.multiple.quartz.config;

import com.suny.tenant.mode.multiple.TenantProperties;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * @author sunjianrong
 * @date 2021-05-29 11:14
 */
public class TenantQuartzDataSourceInitializer extends AbstractTenantAbstractDataSourceInitializer{
    private final QuartzProperties properties;

    public TenantQuartzDataSourceInitializer(DataSource dataSource,
                                             ResourceLoader resourceLoader, QuartzProperties properties , TenantProperties dsProperties) {
        super(dataSource, resourceLoader, dsProperties);
        Assert.notNull(properties, "QuartzProperties must not be null");
        this.properties = properties;
    }

    @Override
    protected void customize(ResourceDatabasePopulator populator) {
        populator.setCommentPrefixes(this.properties.getJdbc().getCommentPrefix().toArray(new String[0]));
    }

    @Override
    protected DataSourceInitializationMode getMode() {
        return this.properties.getJdbc().getInitializeSchema();
    }

    @Override
    protected String getSchemaLocation() {
        return this.properties.getJdbc().getSchema();
    }

    @Override
    protected String getDatabaseName() {
        String databaseName = super.getDatabaseName();
        if ("db2".equals(databaseName)) {
            return "db2_v95";
        }
        if ("mysql".equals(databaseName)) {
            return "mysql_innodb";
        }
        if ("postgresql".equals(databaseName)) {
            return "postgres";
        }
        if ("sqlserver".equals(databaseName)) {
            return "sqlServer";
        }
        return databaseName;
    }
}
