package cn.z.tinytask.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

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
@Configuration
@EnableConfigurationProperties(TinyTaskProperties.class)
public class TinyTaskAutoConfiguration {

    /**
     * 日志实例
     */
    private static final Logger log = LoggerFactory.getLogger(TinyTaskAutoConfiguration.class);
    /**
     * 前缀默认值{@value}
     */
    private static final String DEFAULT_PREFIX = "tinytoken";
    /**
     * 过期时间默认值{@value}秒(30天)
     */
    private static final long DEFAULT_TIMEOUT = 2592000L;

    /**
     * TinyTaskProperties
     */
    private final TinyTaskProperties tinyTaskProperties;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTaskProperties TinyTaskProperties
     */
    public TinyTaskAutoConfiguration(TinyTaskProperties tinyTaskProperties) {
        this.tinyTaskProperties = tinyTaskProperties;
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init() {
        String msg = "TinyToken配置：前缀PREFIX ";
        if (tinyTaskProperties.getPrefix() == null) {
            tinyTaskProperties.setPrefix(DEFAULT_PREFIX);
            msg += DEFAULT_PREFIX + " (默认)";
        } else {
            msg += tinyTaskProperties.getPrefix() + " ";
        }
        msg += "，过期时间TIMEOUT ";
        if (tinyTaskProperties.getTimeout() == null) {
            tinyTaskProperties.setTimeout(DEFAULT_TIMEOUT);
            msg += DEFAULT_TIMEOUT + " (秒)[30天](默认)";
        } else {
            msg += tinyTaskProperties.getTimeout() + " (秒)";
        }
        log.info(msg);
    }

}
