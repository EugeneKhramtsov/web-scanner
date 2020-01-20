package com.demo.webscanner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {

    @Value( "${spring.task.execution.pool.core-size}" )
    private int corePoolSize;

    @Value( "${spring.task.execution.pool.max-size}" )
    private int maxPoolSize;

    @Value( "${spring.task.execution.pool.thread-name-prefix}" )
    private String threadNamePrefix;

    @Value( "${spring.task.execution.pool.keep-alive}" )
    private int keepAlive;

    @Value( "${spring.task.execution.pool.queue-capacity}" )
    private int queueCapacity;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setThreadNamePrefix(threadNamePrefix);
        taskExecutor.setKeepAliveSeconds(keepAlive);
        taskExecutor.setQueueCapacity(queueCapacity);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
