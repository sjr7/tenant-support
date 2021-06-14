package com.suny.tenant.mode.multiple.auth;

import com.suny.tenant.api.auth.AbstractLoginInfoContext;
import com.suny.tenant.constant.TenantSupportConstant;
import com.suny.tenant.mode.multiple.helper.WebRequestHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * AMI内网服务免鉴权方案
 *
 * @author sunjianrong
 * @date 2021-04-23 14:26
 */
// @Service
@Slf4j
// @ConditionalOnMissingBean(value = {ILoginInfoContext.class})
public class RequestHeaderLoginInfoContextImpl extends AbstractLoginInfoContext {

    @Override
    public String getTenantId() {
        return WebRequestHelper.getHeader(TenantSupportConstant.REQUEST_HEADER_TENANT_ID);
    }

}
