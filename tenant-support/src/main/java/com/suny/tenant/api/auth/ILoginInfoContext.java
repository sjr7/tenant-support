package com.suny.tenant.api.auth;

/**
 * @author sunjianrong
 * @date 2021-04-22 17:37
 */
public interface ILoginInfoContext {
    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    String getTenantId();


    /**
     * 是否存在用户登陆信息
     *
     * @return 是否存在用户登陆信息
     */
    boolean existLoginInfo();
}
