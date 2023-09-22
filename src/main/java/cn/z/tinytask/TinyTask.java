package cn.z.tinytask;

import java.util.List;

/**
 * <h1>轻量级集群任务接口</h1>
 *
 * <p>
 * createDate 2023/07/24 10:09:31
 * </p>
 *
 * @param <I> id类型<br>
 *            推荐Long、Integer、String
 * @param <K> token类型<br>
 *            推荐String
 * @param <V> 拓展内容类型
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public interface TinyTask<I, K, V> {

    /**
     * 设置token(token使用UUID 过期时间使用默认值)
     *
     * @param id id
     * @return token
     */
    K setToken(I id);

    /**
     * 设置token(token使用UUID)
     *
     * @param id      id
     * @param timeout 过期时间(秒)
     * @return token
     */
    K setToken(I id, long timeout);

    /**
     * 设置token(过期时间使用默认值)
     *
     * @param id    id
     * @param token token
     */
    void setToken(I id, K token);

    /**
     * 设置token
     *
     * @param id      id
     * @param token   token
     * @param timeout 过期时间(秒)
     */
    void setToken(I id, K token, long timeout);

    /**
     * 设置token
     *
     * @param id      id
     * @param token   token
     * @param extra   拓展内容
     * @param timeout 过期时间(秒)
     */
    void setToken(I id, K token, V extra, long timeout);

    /**
     * 获取token(当前Context 不判断是否有效)
     *
     * @return token(不存在返回null)
     */
    K getToken();

    /**
     * 获取token(通过当前Context 判断是否有效)
     *
     * @return token(不存在或无效返回null)
     */
    K getTokenValid();

    /**
     * 获取token列表
     *
     * @param id id
     * @return token列表(不存在返回[])
     */
    List<K> getToken(I id);

    /**
     * 获取id(当前Context)
     *
     * @return id(不存在返回null)
     */
    I getId();

    /**
     * 获取id
     *
     * @param token token
     * @return id(不存在返回null)
     */
    I getId(K token);

    /**
     * token是否存在，通过当前Context
     *
     * @return 是否存在
     */
    boolean existByToken();

    /**
     * token是否存在
     *
     * @param token token
     * @return 是否存在
     */
    boolean existByToken(K token);

    /**
     * id是否存在
     *
     * @param id id
     * @return 是否存在
     */
    boolean existById(I id);

    /**
     * 删除(当前Context)
     *
     * @return 是否成功
     */
    Boolean deleteByToken();

    /**
     * 删除
     *
     * @param token token
     * @return 是否成功
     */
    Boolean deleteByToken(K token);

    /**
     * 删除
     *
     * @param id id
     * @return 成功个数
     */
    Long deleteById(I id);

    /**
     * 设置过期时间(当前Context)
     *
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    Boolean expire(long timeout);

    /**
     * 设置过期时间
     *
     * @param token   token
     * @param timeout 过期时间(秒)
     * @return 是否成功
     */
    Boolean expire(K token, long timeout);

    /**
     * 设置永不过期(当前Context)
     *
     * @return 是否成功
     */
    Boolean persist();

    /**
     * 设置永不过期
     *
     * @param token token
     * @return 是否成功
     */
    Boolean persist(K token);

    /**
     * 获取信息(当前Context)
     *
     * @return 信息(不存在返回null)
     */
    Info<I, K> getInfoByToken();

    /**
     * 获取信息
     *
     * @param token token
     * @return 信息(不存在返回null)
     */
    Info<I, K> getInfoByToken(K token);

    /**
     * 获取信息列表
     *
     * @param id id
     * @return 信息列表(不存在返回[])
     */
    List<Info<I, K>> getInfoById(I id);

    /**
     * 获取信息拓展(当前Context)
     *
     * @return 信息拓展(不存在返回null)
     */
    InfoExtra<I, K, V> getInfoExtraByToken();

    /**
     * 获取信息拓展
     *
     * @param token token
     * @return 信息拓展(不存在返回null)
     */
    InfoExtra<I, K, V> getInfoExtraByToken(K token);

    /**
     * 获取信息拓展列表
     *
     * @param id id
     * @return 信息拓展列表(不存在返回[])
     */
    List<InfoExtra<I, K, V>> getInfoExtraById(I id);

    /**
     * 信息
     *
     * @param <I> id类型<br>
     *            推荐Long、Integer、String
     * @param <K> token类型<br>
     *            推荐String
     */
    class Info<I, K> {

        /**
         * id
         */
        private final I id;
        /**
         * token
         */
        private final K token;
        /**
         * 过期时间(秒)<br>
         * -1:不过期
         */
        private final long timeout;

        private Info() {
            this.token = null;
            this.id = null;
            this.timeout = -1;
        }

        public Info(I id, K token, long timeout) {
            this.token = token;
            this.id = id;
            this.timeout = timeout;
        }

        public K getToken() {
            return token;
        }

        public I getId() {
            return id;
        }

        public long getTimeout() {
            return timeout;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "id=" + id +
                    ", token=" + token +
                    ", timeout=" + timeout +
                    '}';
        }

    }

    /**
     * 信息拓展
     *
     * @param <I> id类型<br>
     *            推荐Long、Integer、String
     * @param <K> token类型<br>
     *            推荐String
     * @param <V> 拓展内容类型
     */
    class InfoExtra<I, K, V> extends Info<I, K> {

        /**
         * 拓展内容
         */
        private final V extra;

        private InfoExtra() {
            this.extra = null;
        }

        public InfoExtra(I id, K token, V extra, long timeout) {
            super(id, token, timeout);
            this.extra = extra;
        }

        public V getExtra() {
            return extra;
        }

        @Override
        public String toString() {
            return "InfoExtra{" +
                    "id=" + super.getId() +
                    ", token=" + super.getToken() +
                    ", timeout=" + super.getTimeout() +
                    ", extra=" + extra +
                    '}';
        }

    }

}
