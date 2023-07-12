package dingding.bot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolConfig {
    @Value("${Thread.CorePoolSize}")
    private Integer CorePoolSize;
    @Value("${Thread.MaxPoolSize}")
    private Integer MaxPoolSize;
    @Value("${Thread.QueueCapacity}")
    private Integer QueueCapacity;
    @Value("${Thread.ThreadNamePrefix}")
    private String ThreadNamePrefix;


    @Bean
    @Qualifier("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CorePoolSize); // 设置核心线程数
        executor.setMaxPoolSize(MaxPoolSize); // 设置最大线程数
        executor.setQueueCapacity(QueueCapacity); // 设置队列容量
        executor.setThreadNamePrefix(ThreadNamePrefix); // 设置线程名称前缀

        // 设置线程池的拒绝策略，当队列和最大线程池都满了，如何处理新任务
        // 这里使用了默认的AbortPolicy，表示丢弃任务并抛出RejectedExecutionException异常
        // 如果需要自定义拒绝策略，可以实现RejectedExecutionHandler接口并设置到executor中
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        executor.initialize(); // 初始化线程池
        return executor;
    }
}

