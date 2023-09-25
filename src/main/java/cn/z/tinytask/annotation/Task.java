package cn.z.tinytask.annotation;

/**
 * <h1>轻量级集群任务</h1>
 *
 * <p>
 * createDate 2023/09/25 15:59:23
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public @interface Task {

    /**
     * 自动运行(默认true)
     */
    boolean autoRun() default true;

    /**
     * cron表达式(默认""不使用cron表达式)
     */
    String cron() default "";

    /**
     * 循环执行初始延迟时间(秒)(默认0立即执行)
     */
    long delay() default 0L;

    /**
     * 循环执行间隔时间(秒)(默认-1不循环执行)
     */
    long period() default -1L;

    /**
     * 增强循环执行(下一次执行时间=上一次执行时间+循环执行间隔时间)(默认false)
     */
    boolean expand() default false;

}
