package com.suny.tenant.annotations;

import com.suny.tenant.mode.single.SingleTenantModelConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author sunjianrong
 * @date 2021-05-13 10:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({SingleTenantModelConfiguration.class})
public @interface EnableSingleTenant {
}
