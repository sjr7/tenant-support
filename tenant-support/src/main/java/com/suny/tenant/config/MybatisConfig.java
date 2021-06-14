package com.suny.tenant.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sunjianrong
 * @date 2021-06-05 11:29
 */
@Configuration
@MapperScan("com.suny.tenant.**.mapper")
public class MybatisConfig {
}
