package com.suny.tenant.samples.redis;

import com.suny.tenant.annotations.EnabledMultipleTenant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sunjianrong
 * @date 2021/7/12 下午11:04
 */
@EnabledMultipleTenant
@SpringBootApplication
public class SpringDataRedisSamples {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataRedisSamples.class, args);
    }
}
