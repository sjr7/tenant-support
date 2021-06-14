package com.suny.tenant.system.datasource;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author sunjianrong
 * @date 2021-04-22 10:12
 */
@Data
public class SysDataSource implements Serializable {
    /**
     * 数据源ID
     */
    private Integer datasourceId;

    /**
     * 连接地址
     */
    private String url;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 属于哪个租户
     */
    private String tenantId;

    /**
     * 数据源名字
     */
    private String datasourceName;

    private static final long serialVersionUID = 1L;
}