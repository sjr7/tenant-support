package com.suny.tenant.mode.multiple.ds.dsprocess;

import com.baomidou.dynamic.datasource.provider.YmlDynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author sunjianrong
 * @date 2021-04-28 16:39
 */
@Slf4j
public class AmiYmlDynamicDataSourceProvider extends YmlDynamicDataSourceProvider {

    public AmiYmlDynamicDataSourceProvider(Map<String, DataSourceProperty> dataSourcePropertiesMap) {
        super(dataSourcePropertiesMap);
    }
}
