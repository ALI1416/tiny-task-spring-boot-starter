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
@ConfigurationProperties(prefix = "tiny-task")
public class TinyTaskProperties {

    /**
     * 前缀(默认值"tinytask")
     */
    private String prefix = "tinytask";
    /**
     * 过期时间(秒)(默认值5)
     */
    private long timeout = 5L;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

}
