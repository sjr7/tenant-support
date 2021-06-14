package com.suny.tenant.mode.multiple.asynctask.decorator;

import java.util.concurrent.Future;

/**
 * @author sunjianrong
 * @date 2021-05-07 18:02
 */
@FunctionalInterface
public interface AsyncReturnValueDecorator<R> {

    Future<R> submit();

}
