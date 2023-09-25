package cn.z.tinytask;

import cn.z.id.Id;
import cn.z.tinytask.annotation.TaskInfo;
import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
@Component
public class T4s implements TinyTask<Long> {

    /**
     * 准备执行
     */
    private static final String READY = "ready";
    /**
     * 正在执行
     */
    private static final String RUNNING = "running";

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
    @Override
    public Long setReady(String methodName) {
        Long taskId = Id.next();
        setReady(methodName, taskId);
        return taskId;
    }

    /**
     * 设置准备执行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     */
    @Override
    public void setReady(String methodName, Long taskId) {
        rt.set(prefix + ":" + READY + ":" + methodName + ":" + taskId, timeout);
    }

    /**
     * 获取准备执行的键
     *
     * @param methodName 方法名
     * @return 键(不存在返回null)
     */
    private String getReadyKey(String methodName) {
        List<String> scan = rt.scan(prefix + ":" + READY + ":" + methodName + ":*");
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
    @Override
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
     * 设置开始执行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     */
    @Override
    public void setBegin(String methodName, Long taskId) {
        rt.set(prefix + ":" + READY + ":" + methodName + ":" + taskId);
    }

    /**
     * 设置结束执行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     */
    @Override
    public Boolean setEnd(String methodName, Long taskId) {
        return rt.delete(prefix + ":" + READY + ":" + methodName + ":" + taskId);
    }

    /**
     * 获取正在执行的键
     *
     * @return 键(不存在返回null)
     */
    private List<String> getRunningKey() {
        return rt.scan(prefix + ":" + RUNNING + ":*");
    }

    /**
     * 获取正在执行的任务
     *
     * @return 任务信息列表
     */
    @Override
    public List<TaskInfo<Long>> getRunning() {
        List<TaskInfo<Long>> list = new ArrayList<>();
        List<String> keys = getRunningKey();
        for (String key : keys) {
            String[] split = key.split(":", -1);
            if (split.length == 4) {
                TaskInfo<Long> info = new TaskInfo<>();
                // TODO
                info.setMethodName(split[2]);
                info.setTaskId(Long.parseLong(split[3]));
                list.add(info);
            }
        }
        return list;
    }

    /**
     * 获取所有任务
     *
     * @return 任务信息列表
     */
    @Override
    public List<TaskInfo<Long>> getAll() {
        return null;
    }

    /**
     * 手动运行(任务ID使用雪花ID)
     *
     * @param methodName 方法名
     * @return 成功:任务ID 失败:null
     */
    @Override
    public Long manualRun(String methodName) {
        return manualRun(methodName, Id.next());
    }

    /**
     * 手动运行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     * @return 成功:null 失败:正在执行的任务ID
     */
    @Override
    public Long manualRun(String methodName, Long taskId) {
        String readyKey = getReadyKey(methodName);
        // 存在准备执行的任务
        if (readyKey == null) {
            return null;
        }
        List<String> runningKeys = getRunningKey();
        for (String key : runningKeys) {
            String[] split = key.split(":", -1);
            if (split.length == 4 && (methodName.equals(split[2]))) {
                return null;
            }
        }
        // TODO
        return taskId;
    }

    /**
     * 设置开启状态
     *
     * @param methodName 方法名
     * @param open       是否开启
     * @return true:设置成功 false:找不到方法
     */
    @Override
    public boolean setStatus(String methodName, boolean open) {
        return false;
    }

    /**
     * 强制停止任务
     *
     * @param taskId 任务ID
     * @return true:成功 false:找不到任务
     */
    @Override
    public boolean forceStop(Long taskId) {
        return false;
    }

}
