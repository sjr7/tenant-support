package com.suny.tenant.annotations.scheduletask;

import java.lang.annotation.*;

/**
 * @author sunjianrong
 * @date 2021-05-07 11:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantScheduledTask {

    boolean onlyMaster() default false;
}
