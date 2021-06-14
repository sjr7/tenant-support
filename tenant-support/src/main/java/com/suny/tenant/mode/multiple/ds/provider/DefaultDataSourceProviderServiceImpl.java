package com.suny.tenant.mode.multiple.ds.provider;

import com.suny.tenant.system.datasource.SysDataSource;
import com.suny.tenant.system.datasource.SysDbSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-25 10:47
 */
@Service
@Slf4j
// @ConditionalOnMissingBean("dataSourceProviderService")
public class DefaultDataSourceProviderServiceImpl implements DataSourceProviderService {
    private final SysDbSourceService sysDbSourceService;

    public DefaultDataSourceProviderServiceImpl(SysDbSourceService sysDbSourceService) {
        this.sysDbSourceService = sysDbSourceService;
    }

    @Override
    public List<SysDataSource> loadAll() {
        return sysDbSourceService.selectAll();
    }
}
