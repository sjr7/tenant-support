package com.suny.tenant.system.datasource;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.suny.tenant.system.datasource.mapper.SysDbSourceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-22 10:25
 */
@Service
@Slf4j
public class SysDbSourceServiceImpl implements SysDbSourceService {

    @Resource
    private SysDbSourceMapper sysDbSourceMapper;

    @Override
    public List<SysDataSource> selectAll() {
        return sysDbSourceMapper.selectAll();
    }
}
