package com.suny.tenant.mode.multiple.ds.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author sunjianrong
 * @date 2021-04-23 15:29
 */
@Slf4j
public final class DatasourceKeyUtil {

    private static final String SPLIT = "---";

    private DatasourceKeyUtil() {
    }


    /**
     * 生成 Key
     *
     * @param tenantId 租户ID
     * @param dbKey    数据库名
     * @return 数据源 Key
     */
    public static String genKey(String tenantId, String dbKey) {
        return tenantId + SPLIT + dbKey;
    }

    public static boolean isFinalDsKey(String datasourceKey) {
        if (StringUtils.isBlank(datasourceKey)) {
            return false;
        }

        return datasourceKey.contains(SPLIT);
    }

    public static String replaceTenant(String oldKey, String newTenantId) {
        return DatasourceKeyUtil.genKey(newTenantId, DatasourceKeyUtil.parseDbKey(oldKey));
    }

    public static String parseTenantId(String datasourceKey) {
        final String[] splitStrings = datasourceKey.split(SPLIT);
        if (splitStrings.length != 2) {
            log.debug("datasourceKey {} look like illegal", datasourceKey);
            return null;
        }

        return splitStrings[0];
    }


    public static String parseDbKey(String datasourceKey) {
        final String[] splitStrings = datasourceKey.split(SPLIT);
        if (splitStrings.length != 2) {
            log.debug("datasourceKey {} look like illegal", datasourceKey);
            return null;
        }

        return splitStrings[1];
    }
}
