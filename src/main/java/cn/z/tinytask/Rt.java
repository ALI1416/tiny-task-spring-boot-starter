package cn.z.tinytask;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.concurrent.TimeUnit;

/**
 * <h1>Redis模板</h1>
 *
 * <p>
 * createDate 2023/08/01 16:20:58
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class Rt {

    /**
     * Redis模板
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 构造函数(自动注入)
     *
     * @param factory RedisConnectionFactory
     */
    public Rt(RedisConnectionFactory factory) {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // 使用String序列化
        redisTemplate.setDefaultSerializer(StringRedisSerializer.UTF_8);
        redisTemplate.afterPropertiesSet();
    }

    /**
     * 如果key不存在，则放入，并设置超时时间(set)
     *
     * @param key     键
     * @param timeout 超时时间(秒，必须>0)
     * @return 是否成功
     */
    public Boolean setIfAbsent(String key, long timeout) {
        return redisTemplate.opsForValue().setIfAbsent(key, "", timeout, TimeUnit.SECONDS);
    }

}
