package com.suny.tenant.annotations.common;

import java.lang.annotation.*;

/**
 * @author sunjianrong
 * @date 2021-05-11 16:44
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantCycleExecute {
}
