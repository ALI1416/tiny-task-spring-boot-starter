package cn.z.tinytask.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>轻量级集群任务配置属性</h1>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = TinyTaskProperties.TINY_TASK_PREFIX)
public class TinyTaskProperties {

    /**
     * 前缀{@value}
     */
    public static final String TINY_TASK_PREFIX = "tiny-task";

    /**
     * 前缀(默认值"tinytask")
     */
    private String prefix;
    /**
     * 过期时间(秒)(默认值5)
     */
    private Long timeout;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

}