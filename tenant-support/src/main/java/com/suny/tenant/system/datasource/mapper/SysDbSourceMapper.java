package com.suny.tenant.system.datasource.mapper;

import com.suny.tenant.system.datasource.SysDataSource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-22 10:15
 */
@Mapper
public interface SysDbSourceMapper {
   List<SysDataSource> selectAll();
}




