package cn.z.tinytask;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Redis模板类</h1>
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
     * 秒
     */
    private static final TimeUnit SECONDS = TimeUnit.SECONDS;
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
        RedisSerializer<String> stringRedisSerializer = RedisSerializer.string();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        redisTemplate.afterPropertiesSet();
    }

    /**
     * 删除key(del)
     *
     * @param key 键
     * @return 是否成功
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 模糊查询，一次扫描1000条(scan)
     *
     * @param match 匹配模式<br>
     *              * : 匹配0+个任意字符<br>
     *              ? : 匹配1个任意字符<br>
     *              [abc] : 匹配1个指定字符(括号内字符abc)<br>
     *              [^abc] : 不匹配1个指定字符(括号内字符abc)<br>
     *              [A-z] : 匹配1个指定字符(括号内字符A-z)<br>
     *              \ : 转义(字符*?[]^-\等)
     * @return 键列表
     */
    public List<String> scan(String match) {
        List<String> list = new ArrayList<>();
        try (Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions().match(match).count(1000).build())) {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        return list;
    }

    /**
     * 放入
     *
     * @param key 键(已存在会被覆盖)
     */
    public void set(String key) {
        redisTemplate.opsForValue().set(key, "");
    }

    /**
     * 放入，并设置超时时间(setEX)
     *
     * @param key     键(已存在会被覆盖)
     * @param timeout 超时时间(秒，必须>0)
     */
    public void set(String key, long timeout) {
        redisTemplate.opsForValue().set(key, "", timeout, SECONDS);
    }

}
