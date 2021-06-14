package com.suny.tenant.mode.multiple.asynctask;

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author sunjianrong
 * @date 2021-05-07 16:52
 */
@Component
@Slf4j
public class ExecutorWrapperProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractExecutorService || bean instanceof Executors) {
            log.info("enhanced  executor {}", bean.getClass().getName());
            return TtlExecutors.getTtlExecutor((Executor) bean);
        }
        return bean;
    }

}
