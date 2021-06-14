package com.suny.tenant.mode.single.auth;

import com.suny.tenant.api.auth.AbstractLoginInfoContext;
import com.suny.tenant.api.auth.ILoginInfoContext;
import com.suny.tenant.mode.single.constant.SingleModelConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author sunjianrong
 * @date 2021-05-13 11:14
 */
@Component
@Slf4j
@ConditionalOnMissingBean(value = {ILoginInfoContext.class})
public class DefaultSingleAuthContextImpl extends AbstractLoginInfoContext {
    @Override
    public String getTenantId() {
        return SingleModelConstant.STANDARD_MODE_DEFAULT_TENANT_ID;
    }
}
