package com.suny.tenant.mode.multiple.quartz.config;

import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.thread.UserContext;
import org.quartz.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSourceInitializer;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * @author sunjianrong
 * @date 2021-05-28 16:21
 */
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class,
        PlatformTransactionManager.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@Configuration
public class TenantQuartzAutoConfig {

    @Autowired
    private QuartzProperties properties;
    @Autowired
    private ObjectProvider<SchedulerFactoryBeanCustomizer> customizers;
    @Autowired
    private ObjectProvider<JobDetail> jobDetails;
    @Autowired(required = false)
    private Map<String, Calendar> calendars;
    @Autowired
    private ObjectProvider<Trigger> triggers;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TenantProperties tenantProperties;

    @Autowired
    private JobListener jobListener;

    @Bean
    @ConditionalOnMissingBean
    public QuartzDataSourceInitializer quartzDataSourceInitializer() {
        return null;
    }

    @Primary
    @Bean
    // @ConditionalOnMissingBean
    public TenantSchedulerFactoryBean quartzScheduler() {
        TenantSchedulerFactoryBean schedulerFactoryBean = new TenantSchedulerFactoryBean(tenantProperties);
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setGlobalJobListeners(jobListener);
        if (properties.getSchedulerName() != null) {
            schedulerFactoryBean.setSchedulerName(properties.getSchedulerName());
        }
        schedulerFactoryBean.setAutoStartup(properties.isAutoStartup());
        schedulerFactoryBean.setStartupDelay((int) properties.getStartupDelay().getSeconds());
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(properties.isWaitForJobsToCompleteOnShutdown());
        schedulerFactoryBean.setOverwriteExistingJobs(properties.isOverwriteExistingJobs());
        if (!properties.getProperties().isEmpty()) {
            schedulerFactoryBean.setQuartzProperties(asProperties(properties.getProperties()));
        }
        schedulerFactoryBean.setJobDetails(jobDetails.orderedStream().toArray(JobDetail[]::new));
        schedulerFactoryBean.setCalendars(calendars);
        schedulerFactoryBean.setTriggers(triggers.orderedStream().toArray(Trigger[]::new));
        customizers.orderedStream().forEach((customizer) -> customizer.customize(schedulerFactoryBean));
        return schedulerFactoryBean;
    }


    private Properties asProperties(Map<String, String> source) {
        Properties properties = new Properties();
        properties.putAll(source);
        return properties;
    }


    public TenantQuartzDataSourceInitializer quartzDataSourceInitializerProxy(DataSource dataSource,
                                                                              ObjectProvider<DataSource> quartzDataSource,
                                                                              ResourceLoader resourceLoader, QuartzProperties properties, TenantProperties dsProperties) {
        try {
            UserContext.setTenantId(tenantProperties.getDefaultTenantId());
            DataSource dataSourceToUse = getDataSource(dataSource, quartzDataSource);
            return new TenantQuartzDataSourceInitializer(dataSourceToUse, resourceLoader,
                    properties, dsProperties);
        } finally {
            UserContext.clear();
        }


    }

    @Bean
    @ConditionalOnMissingBean
    public TenantQuartzDataSourceInitializer tenantQuartzDataSourceInitializer(
            DataSource dataSource,
            ObjectProvider<DataSource> quartzDataSource,
            ResourceLoader resourceLoader, QuartzProperties properties) {
        return quartzDataSourceInitializerProxy(dataSource, quartzDataSource, resourceLoader, properties, tenantProperties);
    }

    private DataSource getDataSource(DataSource dataSource,
                                     ObjectProvider<DataSource> quartzDataSource) {
        DataSource dataSourceIfAvailable = quartzDataSource.getIfAvailable();
        return (dataSourceIfAvailable != null) ? dataSourceIfAvailable : dataSource;
    }
}
