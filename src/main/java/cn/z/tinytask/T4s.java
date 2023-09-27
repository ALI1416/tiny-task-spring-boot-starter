package cn.z.tinytask;

import cn.z.id.Id;
import cn.z.tinytask.annotation.TaskInfo;
import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * <h1>轻量级集群任务Spring实现</h1>
 *
 * <p>
 * createDate 2023/09/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class T4s {

    /**
     * 前缀
     */
    private final String prefix;
    /**
     * 过期时间(秒)
     */
    private final long timeout;
    /**
     * Redis模板类
     */
    private final Rt rt;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTaskProperties TinyTokenProperties
     * @param rt                 Rt
     */
    public T4s(TinyTaskProperties tinyTaskProperties, Rt rt) {
        this.prefix = tinyTaskProperties.getPrefix();
        this.timeout = tinyTaskProperties.getTimeout();
        this.rt = rt;
    }

    /**
     * 设置准备执行(任务ID使用雪花ID)
     *
     * @param methodName 方法名
     * @return 任务ID
     */
    @Scheduled
    public long setReady(String methodName) {
        long taskId = Id.next();
        setReady(methodName, taskId);
        return taskId;
    }

    /**
     * 设置准备执行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     */
    public void setReady(String methodName, long taskId) {
        rt.set(prefix + ":" + methodName + ":" + taskId, timeout);
    }

    /**
     * 获取准备执行的键
     *
     * @param methodName 方法名
     * @return 键(不存在返回null)
     */
    private String getReadyKey(String methodName) {
        List<String> scan = rt.scan(prefix + ":" + methodName + ":*");
        if (!scan.isEmpty()) {
            return scan.get(0);
        }
        return null;
    }

    /**
     * 获取准备执行的任务ID
     *
     * @param methodName 方法名
     * @return 存在:任务ID 不存在:null
     */
    public Long getReady(String methodName) {
        String key = getReadyKey(methodName);
        if (key != null) {
            String[] split = key.split(":", -1);
            if (split.length == 4) {
                return Long.parseLong(split[3]);
            }
        }
        return null;
    }

    /**
     * 获取所有任务
     *
     * @return 任务信息列表
     */
    public List<TaskInfo<Long>> getAll() {
        return null;
    }

}
