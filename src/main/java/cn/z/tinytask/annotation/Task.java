package cn.z.tinytask.annotation;

import java.lang.annotation.*;
import java.util.TimeZone;

/**
 * <h1>轻量级集群任务</h1>
 *
 * <p>
 * 例如：每分钟的0秒执行一次<br>
 * <code>
 * &#64;Task("0 * * * * *")<br>
 * public void test() {<br>
 * }
 * </code>
 * </p>
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
     * cron表达式
     */
    String value();

    /**
     * 时区(默认""使用本地时区)
     *
     * @see TimeZone#getTimeZone(String)
     */
    String zone() default "";

}
