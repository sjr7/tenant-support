package com.suny.tenant.system.tenant;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.suny.tenant.system.tenant.mapper.SysTenantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunjianrong
 * @date 2021-04-22 10:26
 */
@Service
@Slf4j
public class SysTenantServiceImpl implements SysTenantService {

    @Resource
    private SysTenantMapper sysTenantMapper;


    @Override
    public List<SysTenant> selectAll() {
        return sysTenantMapper.selectAll();
    }
}
