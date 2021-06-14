package com.suny.tenant.system.tenant;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sunjianrong
 * @date 2021-04-22 10:21
 */
@Data
public class SysTenant implements Serializable {
    /**
     * 主键ID
     */
    private String tenantId;

    /**
     * 租户名字
     */
    private String tenantName;

    /**
     * 租户代码
     */
    private String tenantCode;

    /**
     * 是否是系统租户
     */
    private Boolean systemTenant;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否激活
     */
    private Boolean active;
}