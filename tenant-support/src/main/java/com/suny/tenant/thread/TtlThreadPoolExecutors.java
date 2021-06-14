package com.suny.tenant.thread;

/**
 * @author sunjianrong
 * @date 2021-04-29 11:04
 */
public class TtlThreadPoolExecutors {
/*
    private static final String COMMON_BUSINESS = "COMMON_EXECUTOR";

    public static final int QUEUE_CAPACITY = 20000;

    public static ExecutorService getExecutorService() {
        return TtlExecutorServiceMananger.getExecutorService(COMMON_BUSINESS);
    }

    public static ExecutorService getExecutorService(String threadGroupName) {
        return TtlExecutorServiceMananger.getExecutorService(threadGroupName);
    }

    public static ThreadPoolTaskExecutor getAsyncExecutor() {
        // 用TtlExecutors装饰Executor，结合TransmittableThreadLocal解决异步线程threadlocal传递问题
        return getTtlThreadPoolTaskExecutor(initTaskExecutor());
    }

    private static ThreadPoolTaskExecutor initTaskExecutor () {
        return initTaskExecutor(TtlThreadPoolFactory.DEFAULT_CORE_SIZE, TtlThreadPoolFactory.DEFAULT_POOL_SIZE, QUEUE_CAPACITY);
    }

    private static ThreadPoolTaskExecutor initTaskExecutor (int coreSize, int poolSize, int executorQueueCapacity) {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(coreSize);
        taskExecutor.setMaxPoolSize(poolSize);
        taskExecutor.setQueueCapacity(executorQueueCapacity);
        taskExecutor.setKeepAliveSeconds(120);
        taskExecutor.setAllowCoreThreadTimeOut(true);
        taskExecutor.setThreadNamePrefix("TaskExecutor-ttl");
        taskExecutor.initialize();
        return taskExecutor;
    }

    private static ThreadPoolTaskExecutor getTtlThreadPoolTaskExecutor(ThreadPoolTaskExecutor executor) {
        if (null == executor || executor instanceof ThreadPoolTaskExecutorWrapper) {
            return executor;
        }
        return new ThreadPoolTaskExecutorWrapper(executor);
    }*/

}
