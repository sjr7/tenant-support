package com.suny.tenant.system.datasource;

import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-22 10:23
 */
public interface SysDbSourceService {

    List<SysDataSource> selectAll();
}
