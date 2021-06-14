package com.suny.tenant.mode.multiple.asynctask;

import com.suny.tenant.mode.multiple.asynctask.decorator.AsyncReturnValueDecorator;
import com.suny.tenant.mode.multiple.asynctask.decorator.AsyncVoidDecorator;
import com.suny.tenant.thread.UserContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Future;

/**
 * @author sunjianrong
 * @date 2021-05-07 17:36
 */
@Slf4j
public class AsyncWrapperExecutor {


    /**
     * 执行业务并返回结果
     *
     * @param tenantId  租户ID
     * @param decorator 装饰器
     * @param <R>       泛型结果
     * @return 异步执行结果
     */
    public static <R> Future<R> submit(String tenantId, AsyncReturnValueDecorator<R> decorator) {
        try {
            UserContext.setTenantId(tenantId);
            return decorator.submit();
        } finally {
            UserContext.clear();
        }
    }


    /**
     * 执行业务,不返回结果
     *
     * @param tenantId  租户ID
     * @param decorator 装饰器
     */
    public static void execute(String tenantId, AsyncVoidDecorator decorator) {
        try {
            UserContext.setTenantId(tenantId);
            decorator.execute();
        } finally {
            UserContext.clear();
        }

    }
}
