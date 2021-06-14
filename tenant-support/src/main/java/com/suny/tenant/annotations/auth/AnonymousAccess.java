package com.suny.tenant.annotations.auth;

import java.lang.annotation.*;

/**
 * 匿名访问注解
 *
 * @author sunjianrong
 * @date 2021-05-27 20:24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnonymousAccess {
}
