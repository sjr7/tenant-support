package com.suny.tenant.mode.multiple.helper;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author sunjianrong
 * @date 2021-04-28 18:11
 */
public final class EnvironmentHelper {
    private EnvironmentHelper() {
    }

    public static boolean webReuqest() {
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes != null;
    }
}
