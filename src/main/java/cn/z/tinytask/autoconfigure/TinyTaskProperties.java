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
     * rabbit前缀(默认值"tinytask")
     */
    private String prefixRabbit = "tinytask";
    /**
     * redis前缀(默认值"tinytask")
     */
    private String prefixRedis = "tinytask";
    /**
     * rabbit过期时间(秒)(默认值5)
     */
    private long timeoutRabbit = 5L;
    /**
     * redis过期时间(秒)(默认值5)
     */
    private long timeoutRedis = 5L;

    public String getPrefixRabbit() {
        return prefixRabbit;
    }

    public void setPrefixRabbit(String prefixRabbit) {
        this.prefixRabbit = prefixRabbit;
    }

    public String getPrefixRedis() {
        return prefixRedis;
    }

    public void setPrefixRedis(String prefixRedis) {
        this.prefixRedis = prefixRedis;
    }

    public long getTimeoutRabbit() {
        return timeoutRabbit;
    }

    public void setTimeoutRabbit(long timeoutRabbit) {
        this.timeoutRabbit = timeoutRabbit;
    }

    public long getTimeoutRedis() {
        return timeoutRedis;
    }

    public void setTimeoutRedis(long timeoutRedis) {
        this.timeoutRedis = timeoutRedis;
    }

}
