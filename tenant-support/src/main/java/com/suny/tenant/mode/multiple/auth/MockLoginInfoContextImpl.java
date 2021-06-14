package com.suny.tenant.mode.multiple.auth;

import com.suny.tenant.api.auth.AbstractLoginInfoContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunjianrong
 * @date 2021-04-22 17:52
 */
// @Primary
// @Service
@Slf4j
public class MockLoginInfoContextImpl extends AbstractLoginInfoContext {

    @Override
    public String getTenantId() {
        return "74D0243029B54C7E957914CA579E85E2";
    }
}
