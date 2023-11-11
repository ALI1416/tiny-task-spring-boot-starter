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
@EnableConfigurationProperties(TinyTaskProperties.class)
public class TinyTaskAutoConfiguration {

    /**
     * 日志实例
     */
    private static final Logger log = LoggerFactory.getLogger(TinyTaskAutoConfiguration.class);

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTaskProperties TinyTaskProperties
     */
    public TinyTaskAutoConfiguration(TinyTaskProperties tinyTaskProperties) {
        log.info("TinyTask配置：rabbit前缀RABBIT_PREFIX {} ，redis前缀REDIS_PREFIX {} ，rabbit过期时间RABBIT_TIMEOUT {} (秒) ，redis过期时间REDIS_TIMEOUT {} (秒)", tinyTaskProperties.getPrefixRabbit(), tinyTaskProperties.getPrefixRedis(), tinyTaskProperties.getTimeoutRabbit(), tinyTaskProperties.getTimeoutRedis());
    }

}
