package com.suny.tenant.annotations.quartz;

import java.lang.annotation.*;

/**
 * @author sunjianrong
 * @date 2021-06-01 14:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantQuartzExecute {
}
