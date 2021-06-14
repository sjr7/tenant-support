package com.suny.tenant.mode.multiple;

import com.suny.tenant.api.auth.ILoginInfoContext;
import com.suny.tenant.api.cache.keygen.CacheKeyGenerator;
import com.suny.tenant.api.quartz.QuartzKeyProcessor;
import com.suny.tenant.mode.multiple.auth.RequestHeaderLoginInfoContextImpl;
import com.suny.tenant.mode.multiple.cache.keygen.DefaultMultipleTenantCacheKeyGeneratorImpl;
import com.suny.tenant.mode.multiple.ds.config.AmiDsAutoConfiguration;
import com.suny.tenant.mode.multiple.ds.dsprocess.AmiProcessorConfig;
import com.suny.tenant.mode.multiple.helper.TenantCacheKeyHelper;
import com.suny.tenant.mode.multiple.provider.DefaultTenantProviderServiceImpl;
import com.suny.tenant.mode.multiple.provider.TenantProviderService;
import com.suny.tenant.mode.multiple.quartz.TenantQuartzKeyProcessor;
import com.suny.tenant.system.tenant.SysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;

import javax.annotation.Resource;

/**
 * 多租户模式配置
 *
 * @author sunjianrong
 * @date 2021-05-13 11:09
 */
@Configuration
@Import({AmiProcessorConfig.class, AmiDsAutoConfiguration.class})
@ComponentScan(basePackages = "com.cie.tenant", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.suny.tenant.mode.single.*")
})
public class MultipleTenantModelConfiguration {
    @Resource
    private SysTenantService sysTenantService;

    @Autowired
    private TenantCacheKeyHelper tenantCacheKeyHelper;


    @Bean
    @ConditionalOnMissingBean
    public TenantProviderService tenantProviderService() {
        return new DefaultTenantProviderServiceImpl(sysTenantService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoginInfoContext loginInfoContext() {
        return new RequestHeaderLoginInfoContextImpl();
    }


    @ConditionalOnMissingBean
    @Bean
    public CacheKeyGenerator cacheKeyGenerator() {
        return new DefaultMultipleTenantCacheKeyGeneratorImpl(tenantCacheKeyHelper);
    }


    // @ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class,
    //         PlatformTransactionManager.class})
    @ConditionalOnMissingBean
    @Bean
    public QuartzKeyProcessor quartzKeyProcessor() {
        return new TenantQuartzKeyProcessor();
    }
}
