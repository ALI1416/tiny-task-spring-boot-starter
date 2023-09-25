package cn.z.tinytask.annotation;

import java.sql.Timestamp;

/**
 * <h1>轻量级集群任务信息</h1>
 *
 * <p>
 * createDate 2023/09/25 16:10:53
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class TaskInfo<I> {

    /**
     * 开启
     */
    boolean open;
    /**
     * 方法名
     */
    String methodName;
    /**
     * 正在执行的任务ID(null:现在未在执行)
     */
    I taskId;
    /**
     * 现在是否为自动执行模式(false:手动执行 null:现在未在执行)
     */
    Boolean autoRunning;
    /**
     * cron表达式
     */
    String cron;
    /**
     * 循环执行初始延迟时间(秒)
     */
    Long delay;
    /**
     * 循环执行间隔时间(秒)
     */
    Long period;
    /**
     * 增强循环执行(下一次执行时间=上一次执行时间+循环执行间隔时间)
     */
    Boolean expand;
    /**
     * 上一次执行时间(包含正在执行)(null:未执行过)
     */
    Timestamp lastTime;
    /**
     * 下一次执行时间
     */
    Timestamp nextTime;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public I getTaskId() {
        return taskId;
    }

    public void setTaskId(I taskId) {
        this.taskId = taskId;
    }

    public Boolean getAutoRunning() {
        return autoRunning;
    }

    public void setAutoRunning(Boolean autoRunning) {
        this.autoRunning = autoRunning;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Boolean getExpand() {
        return expand;
    }

    public void setExpand(Boolean expand) {
        this.expand = expand;
    }

    public Timestamp getLastTime() {
        return lastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    public Timestamp getNextTime() {
        return nextTime;
    }

    public void setNextTime(Timestamp nextTime) {
        this.nextTime = nextTime;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "open=" + open +
                ", methodName='" + methodName + '\'' +
                ", taskId=" + taskId +
                ", autoRunning=" + autoRunning +
                ", cron='" + cron + '\'' +
                ", delay=" + delay +
                ", period=" + period +
                ", expand=" + expand +
                ", lastTime=" + lastTime +
                ", nextTime=" + nextTime +
                '}';
    }

}
