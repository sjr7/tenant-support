package com.suny.tenant.annotations.quartz;

import java.lang.annotation.*;

/**
 * @author sunjianrong
 * @date 2021-05-26 17:01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantQuartzInitWrapper {
}
