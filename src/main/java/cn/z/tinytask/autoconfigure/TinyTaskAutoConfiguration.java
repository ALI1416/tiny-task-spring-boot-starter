package cn.z.tinytask.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <h1>轻量级集群任务自动配置</h1>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 */
@EnableConfigurationProperties({TinyTaskProperties.class, TinyTaskRedisProperties.class, TinyTaskRabbitProperties.class})
public class TinyTaskAutoConfiguration {

    /**
     * 日志实例
     */
    private static final Logger log = LoggerFactory.getLogger(TinyTaskAutoConfiguration.class);

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTaskProperties       TinyTaskProperties
     * @param tinyTaskRedisProperties  TinyTaskRedisProperties
     * @param tinyTaskRabbitProperties TinyTaskRabbitProperties
     */
    public TinyTaskAutoConfiguration(TinyTaskProperties tinyTaskProperties, TinyTaskRedisProperties tinyTaskRedisProperties, TinyTaskRabbitProperties tinyTaskRabbitProperties) {
        log.info("TinyTask配置：线程池数量THREAD_POOL_SIZE {} ，Redis前缀PREFIX {} ，Redis过期时间TIMEOUT {} (秒) ，RabbitMQ前缀PREFIX {} ，RabbitMQ过期时间TIMEOUT {} (秒)",
                tinyTaskProperties.getThreadPoolSize(),
                tinyTaskRedisProperties.getPrefix(), tinyTaskRedisProperties.getTimeout(),
                tinyTaskRabbitProperties.getPrefix(), tinyTaskRabbitProperties.getTimeout()
        );
    }

}
