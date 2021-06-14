package com.suny.tenant.annotations;

import com.suny.tenant.mode.multiple.MultipleTenantModelConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sunjianrong
 * @date 2021-04-25 11:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({MultipleTenantModelConfiguration.class})
public @interface EnabledMultipleTenant {
}
