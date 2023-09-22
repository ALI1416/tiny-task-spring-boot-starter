package cn.z.tinytask;

import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <h1>轻量级集群任务Spring实现</h1>
 *
 * <h3>id类型:Long</h3>
 * <h3>token类型:String</h3>
 * <h3>拓展内容类型:String</h3>
 *
 * <p>
 * 使用Redis储存token<br>
 * 键名：前缀:id:token<br>
 * 值：指定值
 * </p>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Component
public class T4s implements TinyTask<Long, String, String> {

    /**
     * 前缀
     */
    private final String prefix;
    /**
     * 过期时间(秒)
     */
    private final Long timeout;
    /**
     * Redis模板类
     */
    private final Rt rt;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTaskProperties TinyTokenProperties
     * @param rt                  Rt
     */
    public T4s(TinyTaskProperties tinyTaskProperties, Rt rt) {
        this.prefix = tinyTaskProperties.getPrefix();
        this.timeout = tinyTaskProperties.getTimeout();
        this.rt = rt;
    }

    /**
     * 设置token(token使用UUID 过期时间使用默认值)
     *
     * @param id id
     * @return token
     */
    @Override
    public String setToken(Long id) {
        String token = UUID.randomUUID().toString();
        setToken(id, token, "", timeout);
        return token;
    }

    /**
     * 设置token(token使用UUID)
     *
     * @param id      id
     * @param timeout 过期时间(秒)
     * @return token
     */
    @Override
    public String setToken(Long id, long timeout) {
        String token = UUID.randomUUID().toString();
        setToken(id, token, "", timeout);
        return token;
    }

    /**
     * 设置token(过期时间使用默认值)
     *
     * @param id    id
     * @param token token
     */
    @Override
    public void setToken(Long id, String token) {
        setToken(id, token, "", timeout);
    }

    /**
     * 设置token
     *
     * @param id      id
     * @param token   token
     * @param timeout 过期时间(秒)
     */
    @Override
    public void setToken(Long id, String token, long timeout) {
        setToken(id, token, "", timeout);
    }

    /**
     * 设置token
     *
     * @param id      id
     * @param token   token
     * @param extra   拓展内容
     * @param timeout 过期时间(秒)
     */
    @Override
    public void setToken(Long id, String token, String extra, long timeout) {
        rt.set(prefix + ":" + id + ":" + token, extra, timeout);
    }

    /**
     * 获取token(当前Context 不判断是否有效)
     *
     * @return token(不存在返回null)
     * @throws TinyTaskException 不存在Context
     */
    @Override
    public String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new TinyTaskException("不存在Context");
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest().getHeader(prefix);
    }

    /**
     * 获取token(当前Context 判断是否有效)
     *
     * @return token(不存在或无效返回null)
     */
    @Override
    public String getTokenValid() {
        String token = getToken();
        if (token != null && existByToken(token)) {
            return token;
        }
        return null;
    }

    /**
     * 获取键
     *
     * @param token token
     * @return 键(不存在返回null)
     */
    private String getKey(String token) {
        List<String> scan = rt.scan(prefix + ":*:" + token);
        if (!scan.isEmpty()) {
            return scan.get(0);
        }
        return null;
    }

    /**
     * 获取键列表
     *
     * @param id id
     * @return 键列表(不存在返回[])
     */
    private List<String> getKey(Long id) {
        return rt.scan(prefix + ":" + id + ":*");
    }

    /**
     * 获取token列表
     *
     * @param id id
     * @return token列表(不存在返回[])
     */
    @Override
    public List<String> getToken(Long id) {
        List<String> tokens = new ArrayList<>();
        List<String> keys = getKey(id);
        for (String key : keys) {
            String[] split = key.split(":", -1);
            if (split.length == 3) {
                tokens.add(split[2]);
            }
        }
        return tokens;
    }

    /**
     * 获取id(当前Context)
     *
     * @return id(不存在返回null)
     */
    @Override
    public Long getId() {
        String token = getToken();
        if (token != null) {
            return getId(token);
        }
        return null;
    }

    /**
     * 获取id
     *
     * @param token token
     * @return id(不存在返回null)
     */
    @Override
    public Long getId(String token) {
        String key = getKey(token);
        if (key != null) {
            String[] split = key.split(":", -1);
            if (split.length == 3) {
                return Long.parseLong(split[1]);
            }
        }
        return null;
    }

    /**
     * token是否存在(当前Context)
     *
     * @return 是否存在
     */
    @Override
    public boolean existByToken() {
        return getTokenValid() != null;
    }

    /**
     * token是否存在
     *
     * @param token token
     * @return 是否存在
     */
    @Override
    public boolean existByToken(String token) {
        return !rt.scan(prefix + ":*:" + token).isEmpty();
    }

    /**
     * id是否存在
     *
     * @param id id
     * @return 是否存在
     */
    @Override
    public boolean existById(Long id) {
        return !getKey(id).isEmpty();
    }

    /**
     * 删除(当前Context)
     *
     * @return 是否成功
     */
    @Override
    public Boolean deleteByToken() {
        String token = getToken();
        if (token != null) {
            return deleteByToken(token);
        }
        return false;
    }

    /**
     * 删除
     *
     * @param token token
     * @return 是否成功
     */
    @Override
    public Boolean deleteByToken(String token) {
        String key = getKey(token);
        if (key != null) {
            return rt.delete(key);
        }
        return false;
    }

    /**
     * 删除
     *
     * @param id id
     * @return 成功个数
     */
    @Override
    public Long deleteById(Long id) {
        List<String> keys = getKey(id);
        if (!keys.isEmpty()) {
            return rt.deleteMulti(keys);
        }
        return 0L;
    }

    /**
     * 设置过期时间(当前Context)
     *
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    @Override
    public Boolean expire(long timeout) {
        String token = getToken();
        if (token != null) {
            return expire(token, timeout);
        }
        return false;
    }

    /**
     * 设置过期时间
     *
     * @param token   token
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    @Override
    public Boolean expire(String token, long timeout) {
        String key = getKey(token);
        if (key != null) {
            return rt.expire(key, timeout);
        }
        return false;
    }

    /**
     * 设置永不过期(当前Context)
     *
     * @return 是否成功
     */
    @Override
    public Boolean persist() {
        String token = getToken();
        if (token != null) {
            return persist(token);
        }
        return false;
    }

    /**
     * 设置永不过期
     *
     * @param token token
     * @return 是否成功
     */
    @Override
    public Boolean persist(String token) {
        String key = getKey(token);
        if (key != null) {
            return rt.persist(key);
        }
        return false;
    }

    /**
     * 获取信息(当前Context)
     *
     * @return 信息(不存在返回null)
     */
    @Override
    public Info<Long, String> getInfoByToken() {
        String token = getToken();
        if (token != null) {
            return getInfoByToken(token);
        }
        return null;
    }

    /**
     * 获取信息
     *
     * @param token token
     * @return 信息(不存在返回null)
     */
    @Override
    public Info<Long, String> getInfoByToken(String token) {
        String key = getKey(token);
        if (key != null) {
            Long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    return new Info<>(Long.parseLong(split[1]), token, expire);
                }
            }
        }
        return null;
    }

    /**
     * 获取信息列表
     *
     * @param id id
     * @return 信息列表(不存在返回[])
     */
    @Override
    public List<Info<Long, String>> getInfoById(Long id) {
        List<Info<Long, String>> list = new ArrayList<>();
        List<String> keys = getKey(id);
        for (String key : keys) {
            Long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    list.add(new Info<>(id, split[2], expire));
                }
            }
        }
        return list;
    }

    /**
     * 获取信息拓展(当前Context)
     *
     * @return 信息拓展(不存在返回null)
     */
    @Override
    public InfoExtra<Long, String, String> getInfoExtraByToken() {
        String token = getToken();
        if (token != null) {
            return getInfoExtraByToken(token);
        }
        return null;
    }

    /**
     * 获取信息拓展
     *
     * @param token token
     * @return 信息拓展(不存在返回null)
     */
    @Override
    public InfoExtra<Long, String, String> getInfoExtraByToken(String token) {
        String key = getKey(token);
        if (key != null) {
            Long expire = rt.getExpire(key);
            if (expire > -2) {
                String[] split = key.split(":", -1);
                if (split.length == 3) {
                    return new InfoExtra<>(Long.parseLong(split[1]), token, (String) rt.get(key), expire);
                }
            }
        }
        return null;
    }

    /**
     * 获取信息拓展列表
     *
     * @param id id
     * @return 信息拓展列表(不存在返回[])
     */
    @Override
    public List<InfoExtra<Long, String, String>> getInfoExtraById(Long id) {
        List<InfoExtra<Long, String, String>> list = new ArrayList<>();
        List<String> keys = getKey(id);
        if (!keys.isEmpty()) {
            List<Object> extras = rt.getMulti(keys);
            for (int i = 0; i < keys.size(); i++) {
                Long expire = rt.getExpire(keys.get(i));
                if (expire > -2) {
                    String[] split = keys.get(i).split(":", -1);
                    if (split.length == 3) {
                        list.add(new InfoExtra<>(id, split[2], (String) extras.get(i), expire));
                    }
                }
            }
        }
        return list;
    }

}
