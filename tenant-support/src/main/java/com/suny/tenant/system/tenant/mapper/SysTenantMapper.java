package com.suny.tenant.system.tenant.mapper;

import com.suny.tenant.system.tenant.SysTenant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-22 10:25
 */
@Mapper
public interface SysTenantMapper  {

    List<SysTenant> selectAll();
}




