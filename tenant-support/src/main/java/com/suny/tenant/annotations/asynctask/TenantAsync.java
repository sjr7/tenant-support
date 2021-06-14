package com.suny.tenant.annotations.asynctask;

import java.lang.annotation.*;

/**
 * 异步函数标记注解. 当函数上标记 @Async 注解时同时添加此注解
 *
 * @author sunjianrong
 * @date 2021-05-07 16:01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantAsync {
}
