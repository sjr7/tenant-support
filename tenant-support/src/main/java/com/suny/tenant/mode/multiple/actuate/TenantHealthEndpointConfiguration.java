package com.suny.tenant.mode.multiple.actuate;

import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sunjianrong
 * @date 2021-06-02 16:24
 */
// @Configuration(
//         proxyBeanMethods = false
// )
public class TenantHealthEndpointConfiguration {
    // @Bean
    // @ConditionalOnMissingBean
    // HealthEndpoint healthEndpoint(HealthContributorRegistry registry, HealthEndpointGroups groups) {
    //     return new HealthEndpoint(registry, groups);
    // }

}
