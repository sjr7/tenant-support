package com.suny.tenant.utils;

import java.util.Base64;
import java.util.regex.Pattern;

/**
 * @author sunjianrong
 * @date 2021-06-10 16:53
 */
public final class TenantEncryptUtil {
    final static Pattern PATTERN = Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$");


    public static String parseOriginTenantId(String encryptTenantId) {
        if (isBase64(encryptTenantId)) {
            return new String(Base64.getDecoder().decode(encryptTenantId));
        }
        return encryptTenantId;
    }

    public static String encryptTenantId(String originTenantId) {
        return Base64.getEncoder().withoutPadding().encodeToString(originTenantId.getBytes());
    }


    private static boolean isBase64(String str) {
        return PATTERN.matcher(str).matches();
    }
}
