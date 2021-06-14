package com.suny.tenant.mode.multiple.actuate;

import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.thread.UserContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * {@link HealthIndicator} that tests the status of a {@link DataSource} and optionally
 * runs a test query.
 *
 * @author sunjianrong
 * @date 2021-06-03 14:01
 */
public class TenantDataSourceHealthIndicator extends AbstractHealthIndicator implements InitializingBean {
// public class TenantDataSourceHealthIndicator extends DataSourceHealthIndicator {

    private DataSource dataSource;

    private String query;

    private JdbcTemplate jdbcTemplate;

    private TenantProperties tenantProperties;

    /**
     * Create a new {@link DataSourceHealthIndicator} instance.
     */
    private TenantDataSourceHealthIndicator() {
        this(null, null);
    }

    /**
     * Create a new {@link DataSourceHealthIndicator} using the specified
     * {@link DataSource}.
     *
     * @param dataSource the data source
     */
    private TenantDataSourceHealthIndicator(DataSource dataSource) {
        this(dataSource, null);
    }

    /**
     * Create a new {@link DataSourceHealthIndicator} using the specified
     * {@link DataSource} and validation query.
     *
     * @param dataSource the data source
     * @param query      the validation query to use (can be {@code null})
     */
    private TenantDataSourceHealthIndicator(DataSource dataSource, String query) {
        super("DataSource health check failed");
        this.dataSource = dataSource;
        this.query = query;
        this.jdbcTemplate = (dataSource != null) ? new JdbcTemplate(dataSource) : null;
    }


    /**
     * Create a new {@link DataSourceHealthIndicator} using the specified
     * {@link DataSource} and validation query.
     *
     * @param dataSource       the data source
     * @param query            the validation query to use (can be {@code null})
     * @param tenantProperties the tenant properties
     */
    public TenantDataSourceHealthIndicator(DataSource dataSource, String query, TenantProperties tenantProperties) {
        super("DataSource health check failed");
        this.dataSource = dataSource;
        this.query = query;
        this.jdbcTemplate = (dataSource != null) ? new JdbcTemplate(dataSource) : null;
        this.tenantProperties = tenantProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.state(this.dataSource != null, "DataSource for DataSourceHealthIndicator must be specified");
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        if (this.dataSource == null) {
            builder.up().withDetail("database", "unknown");
        } else {
            UserContext.setTenantId(tenantProperties.getDefaultTenantId());
            try {
                doDataSourceHealthCheck(builder);
            } finally {
                UserContext.clear();
            }
        }
    }

    private void doDataSourceHealthCheck(Health.Builder builder) throws Exception {
        builder.up().withDetail("database", getProduct());
        String validationQuery = this.query;
        if (StringUtils.hasText(validationQuery)) {
            builder.withDetail("validationQuery", validationQuery);
            // Avoid calling getObject as it breaks MySQL on Java 7 and later
            List<Object> results = this.jdbcTemplate.query(validationQuery, new TenantDataSourceHealthIndicator.SingleColumnRowMapper());
            Object result = DataAccessUtils.requiredSingleResult(results);
            builder.withDetail("result", result);
        } else {
            builder.withDetail("validationQuery", "isValid()");
            boolean valid = isConnectionValid();
            builder.status((valid) ? Status.UP : Status.DOWN);
        }
    }

    private String getProduct() {
        return this.jdbcTemplate.execute((ConnectionCallback<String>) this::getProduct);
    }

    private String getProduct(Connection connection) throws SQLException {
        return connection.getMetaData().getDatabaseProductName();
    }

    private Boolean isConnectionValid() {
        return this.jdbcTemplate.execute((ConnectionCallback<Boolean>) this::isConnectionValid);
    }

    private Boolean isConnectionValid(Connection connection) throws SQLException {
        return connection.isValid(0);
    }

    /**
     * Set the {@link DataSource} to use.
     *
     * @param dataSource the data source
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Set a specific validation query to use to validate a connection. If none is set, a
     * validation based on {@link Connection#isValid(int)} is used.
     *
     * @param query the validation query to use
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Return the validation query or {@code null}.
     *
     * @return the query
     */
    public String getQuery() {
        return this.query;
    }

    /**
     * {@link RowMapper} that expects and returns results from a single column.
     */
    private static class SingleColumnRowMapper implements RowMapper<Object> {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            if (columns != 1) {
                throw new IncorrectResultSetColumnCountException(1, columns);
            }
            return JdbcUtils.getResultSetValue(rs, 1);
        }

    }
}
