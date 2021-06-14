package com.suny.tenant.mode.multiple.ds.provider;

import com.suny.tenant.system.datasource.SysDataSource;

import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-25 10:31
 */
public interface DataSourceProviderService {

    /**
     * 获取所有数据源
     *
     * @return 所有可用数据源
     */
    List<SysDataSource> loadAll();


}
