package com.suny.tenant.mode.multiple.quartz.debuglog.mapper;

import com.suny.tenant.mode.multiple.quartz.debuglog.QuartzDebugLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author sunjianrong
 * @date 2021-06-02 11:00
 */
@Mapper
public interface QuartzDebugLogMapper {

    int saveLog(@Param("log") QuartzDebugLog quartzDebugLog);
}




