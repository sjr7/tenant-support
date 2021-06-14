package com.suny.tenant.mode.single;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * 单租户模式配置
 *
 * @author sunjianrong
 * @date 2021-05-13 11:06
 */
@Configuration
@ComponentScan(basePackages = "com.cie.tenant", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.suny.tenant.mode.multiple.*")
})
public class SingleTenantModelConfiguration {
}
