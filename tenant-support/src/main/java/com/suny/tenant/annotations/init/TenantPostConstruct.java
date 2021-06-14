package com.suny.tenant.annotations.init;

import java.lang.annotation.*;

/**
 * @author sunjianrong
 * @date 2021-05-10 11:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantPostConstruct {
}
