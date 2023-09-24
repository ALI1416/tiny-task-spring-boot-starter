package cn.z.tinytask.entity;

/**
 * <h1>RabbitMQ消息类型</h1>
 *
 * <p>
 * createDate 2023/09/24 15:28:47
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public enum MsgType {

    /**
     * 执行一次
     */
    ONCE,
    /**
     * 广播
     */
    BROADCAST,
    /**
     * 分片
     */
    SHARDING

}
