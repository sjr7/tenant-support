package com.suny.tenant.annotations.asynctask;

import java.lang.annotation.*;

/**
 * 控制层调用异步函数注解
 *
 * @author sunjianrong
 * @date 2021-05-08 14:04
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantControllerAsync {
}
