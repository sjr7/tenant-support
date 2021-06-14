package com.suny.tenant.mode.multiple.quartz.debuglog;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sunjianrong
 * @date 2021-06-02 11:00
 */
@Data
public class QuartzDebugLog implements Serializable {
    /**
     * debug日志ID
     */
    private Integer id;

    /**
     * job key 
     */
    private String jobKey;

    /**
     * job class
     */
    private String jobClass;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * fire time
     */
    private Date firetime;

    /**
     * scheduled fire time
     */
    private Date scheduledFireTime;

    private static final long serialVersionUID = 1L;
}