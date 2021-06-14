package com.suny.tenant.mode.multiple.helper;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author sunjianrong
 * @date 2021-05-28 9:27
 */
public final class WebRequestHelper {

    public static boolean isWebRequest(){
        final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return requestAttributes != null;
    }

    public static String getHeader(String headerName) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        if (sra == null) {
            throw new RuntimeException("not a web request !");
        }
        HttpServletRequest request = sra.getRequest();
        return request.getHeader(headerName);
    }
}
