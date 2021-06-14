package com.suny.tenant.mode.multiple.web;

import com.suny.tenant.annotations.auth.AnonymousAccess;
import com.suny.tenant.api.auth.ILoginInfoContext;
import com.suny.tenant.constant.TenantSupportConstant;
import com.suny.tenant.mode.multiple.TenantProperties;
import com.suny.tenant.mode.multiple.helper.JoinPointHelper;
import com.suny.tenant.mode.multiple.helper.WebRequestHelper;
import com.suny.tenant.mode.multiple.provider.SystemTenantHelper;
import com.suny.tenant.system.tenant.SysTenant;
import com.suny.tenant.thread.UserContext;
import com.suny.tenant.utils.TenantEncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author sunjianrong
 * @date 2021-05-12 15:38
 */
@Order()
@Aspect
@Component
@Slf4j
public class TenantWebRequestAspect {

    @Autowired
    private SystemTenantHelper systemTenantHelper;

    @Autowired
    private TenantProperties tenantProperties;

    private final ILoginInfoContext loginInfoContext;

    public TenantWebRequestAspect(ILoginInfoContext loginInfoContext) {
        this.loginInfoContext = loginInfoContext;
    }

    @Pointcut("execution(* com.cie..*..*Controller.*(..))")
    public void execute() {
    }

    @Around("execute()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final Method method = JoinPointHelper.getMethod(joinPoint);

        final AnonymousAccess annotation = method.getAnnotation(AnonymousAccess.class);
        String tenantId;
        if (annotation != null) {
            tenantId = WebRequestHelper.getHeader(TenantSupportConstant.REQUEST_HEADER_TENANT_ID);
           /* if (StringUtils.isBlank(tenantId)) {
                throw new RuntimeException("tenant id is required!");
            }*/
            if (StringUtils.isNotBlank(tenantId)) {
                tenantId = TenantEncryptUtil.parseOriginTenantId(tenantId);
            } else {
                tenantId = tenantProperties.getDefaultTenantId();
            }

        } else {
            tenantId = loginInfoContext.getTenantId();
            if (StringUtils.isBlank(tenantId)) {
                throw new RuntimeException("tenant id is required!");
            }
        }


        final SysTenant tenant = systemTenantHelper.getTenant(tenantId);
        if (tenant == null) {
            throw new IllegalArgumentException("Illegal tenant id [" + tenantId + "]");
        }

        try {
            UserContext.setTenantId(tenantId);
            log.debug("Current request tenant id : {}", tenantId);
            return joinPoint.proceed();
        } finally {
            UserContext.clear();
        }
    }


}
