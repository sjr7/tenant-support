package com.suny.tenant.exception;

import com.suny.tenant.mode.multiple.helper.WebRequestHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author sunjianrong
 * @date 2021-06-07 11:12
 */
@EqualsAndHashCode(callSuper = true)
public class ThreadTenantIdNotFoundException extends RuntimeException {

    @Getter
    private final String threadName;
    @Getter
    private final boolean isWebRequest;

    public ThreadTenantIdNotFoundException() {
        threadName = Thread.currentThread().getName();
        isWebRequest = WebRequestHelper.isWebRequest();
    }

    @Override
    public String toString() {
        return "ThreadTenantIdNotFoundException{" +
                "threadName='" + threadName + '\'' +
                ", isWebRequest=" + isWebRequest +
                '}';
    }
}
