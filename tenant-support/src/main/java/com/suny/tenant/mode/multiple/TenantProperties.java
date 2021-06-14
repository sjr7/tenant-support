package com.suny.tenant.mode.multiple;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * @author sunjianrong
 * @date 2021-04-28 15:22
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "tenant")
@Validated
public class TenantProperties {

    /**
     * 平台租户ID
     */
    @NotEmpty
    private String defaultTenantId;

    /**
     * quartz 属性
     */
    private QuartzProperties quartz = new QuartzProperties();

    @Data
    public static class QuartzProperties {
        /**
         * 启用执行 debug 日志
         */
        private boolean enableExecuteDebugLog = false;
    }
}
