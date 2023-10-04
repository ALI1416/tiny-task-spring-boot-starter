package cn.z.tinytask.annotation;

import java.lang.annotation.*;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Task {

    /**
     * cron表达式(默认""不使用cron表达式)
     */
    String cron() default "";

    /**
     * 时区(默认""使用本地时区)<br>
     * 仅对{@link #cron()}有效
     *
     * @see TimeZone#getTimeZone(String)
     */
    String zone() default "";

    /**
     * 循环执行初始延迟时间(默认0立即执行)(默认秒)<br>
     * 仅对{@link #fixedDelay()}、{@link #fixedDelayDuration()}、{@link #fixedRate()}、{@link #fixedRateDuration()}有效
     */
    long initialDelay() default 0L;

    /**
     * 循环执行初始延迟时间Duration字符串(默认""跳过)<br>
     * 仅对{@link #fixedDelay()}、{@link #fixedDelayDuration()}、{@link #fixedRate()}、{@link #fixedRateDuration()}有效
     */
    String initialDelayDuration() default "";

    /**
     * 循环执行间隔时间(默认-1不循环执行)(默认秒)<br>
     * 下一次执行时间=上一次开始执行时间+循环执行间隔时间
     */
    long fixedDelay() default -1L;

    /**
     * 循环执行间隔时间Duration字符串(默认""不循环执行)<br>
     * 下一次执行时间=上一次开始执行时间+循环执行间隔时间
     *
     * @see java.time.Duration
     */
    String fixedDelayDuration() default "";

    /**
     * 增强循环执行间隔时间(默认-1不循环执行)(默认秒)<br>
     * 下一次执行时间=上一次执行完成时间+循环执行间隔时间
     */
    long fixedRate() default -1L;

    /**
     * 增强循环执行间隔时间Duration字符串(默认""不循环执行)<br>
     * 下一次执行时间=上一次执行完成时间+循环执行间隔时间
     *
     * @see java.time.Duration
     */
    String fixedRateDuration() default "";

    /**
     * 时间单位(默认TimeUnit.SECONDS秒)<br>
     * 仅对{@link #initialDelay()}、{@link #fixedDelay()}、{@link #fixedRate()}
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

}
