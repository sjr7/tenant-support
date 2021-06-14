package com.suny.tenant.exception;

/**
 * 数据源不存在异常
 *
 * @author sunjianrong
 * @date 2021-04-23 14:37
 */
public class DatasourceNotExistException extends RuntimeException {
    private final String tenantId;
    private final String datasource;


    public DatasourceNotExistException(String tenantId, String datasource) {
        super("could not find a datasource " + datasource + " . tenant id= " + tenantId);
        this.tenantId = tenantId;
        this.datasource = datasource;

    }
}
