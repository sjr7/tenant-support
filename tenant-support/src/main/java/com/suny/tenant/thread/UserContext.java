package com.suny.tenant.thread;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author sunjianrong
 * @date 2021-04-29 10:58
 */
public final class UserContext implements AutoCloseable {

    private static final ThreadLocal<String> TENANT_ID_LOCAL = new TransmittableThreadLocal<>();

    public static String getTenantId() {
        return TENANT_ID_LOCAL.get();
    }

    public static void setTenantId(String userId) {
        TENANT_ID_LOCAL.set(userId);
    }

    public static void clear() {
        TENANT_ID_LOCAL.remove();
    }

    @Override
    public void close() throws Exception {
        clear();
    }
}
