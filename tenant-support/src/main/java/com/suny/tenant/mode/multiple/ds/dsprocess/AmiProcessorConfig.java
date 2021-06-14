package com.suny.tenant.mode.multiple.ds.dsprocess;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.suny.tenant.api.auth.ILoginInfoContext;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author sunjianrong
 * @date 2021-04-22 15:45
 */
@Configuration
// @ComponentScan(basePackages = "com.cie.ami")
public class AmiProcessorConfig {

    @Resource
    private DynamicDataSourceProperties properties;

    @Resource
    private ILoginInfoContext loginInfoContext;

    @Resource
    private DataSource dataSource;

    @Role(value = BeanDefinition.ROLE_INFRASTRUCTURE)
    @Bean
    // @ConditionalOnMissingBean
    public DynamicDataSourceAnnotationAdvisor dynamicDatasourceAnnotationAdvisor(DsProcessor dsProcessor) {
        AmiDsAnnotationInterceptor interceptor = new AmiDsAnnotationInterceptor((DynamicRoutingDataSource) dataSource, properties.isAllowedPublicOnly(), dsProcessor);
        DynamicDataSourceAnnotationAdvisor advisor = new DynamicDataSourceAnnotationAdvisor(interceptor);
        advisor.setOrder(properties.getOrder());
        return advisor;
    }


    @Bean
    // @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
        if (loginInfoContext == null) {
            throw new RuntimeException("Cann't found instance of  the interface " + ILoginInfoContext.class.getName());
        }

        return new AmiDsProcessor(loginInfoContext);
    }
}
