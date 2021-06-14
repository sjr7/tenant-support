package com.suny.tenant.system;

import com.suny.tenant.mode.multiple.provider.TenantProviderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @author sunjianrong
 * @date 2021-05-31 20:13
 */
// @Configuration
@Slf4j
public class ForceInit {


    // @Bean
    public Void earlyBean(ApplicationContext appContext) {
        appContext.getBeansOfType(TenantProviderService.class);
        log.debug("force init TenantProviderService");
        return null;
    }

}
