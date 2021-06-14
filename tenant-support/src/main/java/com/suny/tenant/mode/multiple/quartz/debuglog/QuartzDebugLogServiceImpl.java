package com.suny.tenant.mode.multiple.quartz.debuglog;

import com.suny.tenant.mode.multiple.quartz.debuglog.mapper.QuartzDebugLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sunjianrong
 * @date 2021-06-02 13:09
 */
@Service
@Slf4j
public class QuartzDebugLogServiceImpl implements QuartzDebugLogService {

    @Autowired
    private QuartzDebugLogMapper quartzDebugLogMapper;

    @Override
    public boolean saveLog(QuartzDebugLog quartzDebugLog) {

        return quartzDebugLogMapper.saveLog(quartzDebugLog) > 0;
    }
}
