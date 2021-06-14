package com.suny.tenant.mode.multiple.quartz;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sunjianrong
 * @date 2021-05-26 16:59
 */
@Data
public class BaseQuartzParam implements Serializable {

    private String tenantId;
}
