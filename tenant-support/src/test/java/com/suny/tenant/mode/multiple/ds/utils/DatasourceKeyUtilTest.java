package com.suny.tenant.mode.multiple.ds.utils;

import org.junit.jupiter.api.Assertions;

/**
 * @author sunjianrong
 * @date 2021-05-07 15:33
 */
class DatasourceKeyUtilTest {

    @org.junit.jupiter.api.Test
    void genKey() {
    }

    @org.junit.jupiter.api.Test
    void isFinalDsKey() {
        Assertions.assertTrue(DatasourceKeyUtil.isFinalDsKey("111111@@hes"));
        Assertions.assertFalse(DatasourceKeyUtil.isFinalDsKey("111111@hes"));
        Assertions.assertFalse(DatasourceKeyUtil.isFinalDsKey("111111hes"));
    }

    @org.junit.jupiter.api.Test
    void replaceTenant() {
    }

    @org.junit.jupiter.api.Test
    void parseTenantId() {
    }

    @org.junit.jupiter.api.Test
    void parseDbKey() {
    }
}