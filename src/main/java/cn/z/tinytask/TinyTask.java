package cn.z.tinytask;

import cn.z.tinytask.annotation.TaskInfo;

import java.util.List;

/**
 * <h1>轻量级集群任务接口</h1>
 *
 * <p>
 * createDate 2023/09/24 10:09:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public interface TinyTask<I> {

    /**
     * 设置准备执行(任务ID使用雪花ID)
     *
     * @param methodName 方法名
     * @return 任务ID
     */
    I setReady(String methodName);

    /**
     * 设置准备执行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     */
    void setReady(String methodName, I taskId);

    /**
     * 获取准备执行的任务ID
     *
     * @param methodName 方法名
     * @return 存在:任务ID 不存在:null
     */
    I getReady(String methodName);

    /**
     * 设置开始执行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     */
    void setBegin(String methodName, I taskId);

    /**
     * 设置结束执行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     */
    Boolean setEnd(String methodName, I taskId);

    /**
     * 获取正在执行的任务
     *
     * @return 任务信息列表
     */
    List<TaskInfo<I>> getRunning();

    /**
     * 获取所有任务
     *
     * @return 任务信息列表
     */
    List<TaskInfo<I>> getAll();

    /**
     * 手动运行(任务ID使用雪花ID)
     *
     * @param methodName 方法名
     * @return 成功:任务ID 失败:null
     */
    I manualRun(String methodName);

    /**
     * 手动运行
     *
     * @param methodName 方法名
     * @param taskId     任务ID
     * @return 成功:null 失败:正在执行的任务ID
     */
    I manualRun(String methodName, I taskId);

    /**
     * 设置开启状态
     *
     * @param methodName 方法名
     * @param open       是否开启
     * @return true:设置成功 false:找不到方法
     */
    boolean setStatus(String methodName, boolean open);

    /**
     * 强制停止任务
     *
     * @param taskId 任务ID
     * @return true:成功 false:找不到任务
     */
    boolean forceStop(I taskId);

}
