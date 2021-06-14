package com.suny.tenant.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sunjianrong
 * @date 2021-06-10 15:39
 */
@Data
public class TenantMsg implements Serializable {

    private String tenantId;
}
