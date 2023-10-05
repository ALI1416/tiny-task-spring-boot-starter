package cn.z.tinytask.annotation;

import cn.z.tinytask.Rt;
import cn.z.tinytask.TinyTaskException;
import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.scheduling.config.FixedRateTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * <h1>轻量级集群任务注解处理</h1>
 *
 * <p>
 * createDate 2023/09/25 16:07:06
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class TaskAnnotationProcessor implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

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
     * RabbitMQ模板
     */
    private final RabbitTemplate rabbitTemplate;
    /**
     * 任务注册器
     */
    private final ScheduledTaskRegistrar registrar;

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTaskProperties TinyTaskProperties
     * @param rt                 Rt
     * @param rabbitTemplate     RabbitTemplate
     */
    public TaskAnnotationProcessor(TinyTaskProperties tinyTaskProperties, Rt rt, RabbitTemplate rabbitTemplate) {
        this.prefix = tinyTaskProperties.getPrefix();
        this.timeout = tinyTaskProperties.getTimeout();
        this.rt = rt;
        this.rabbitTemplate = rabbitTemplate;
        this.registrar = new ScheduledTaskRegistrar();
    }

    /**
     * ApplicationContext
     */
    private static ApplicationContext applicationContext;

    /**
     * ApplicationContextAware
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TaskAnnotationProcessor.applicationContext = applicationContext;
    }

    /**
     * 获取Bean
     *
     * @param beanName Bean名
     * @return Bean
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    /**
     * SmartInitializingSingleton
     */
    @Override
    public void afterSingletonsInstantiated() {
        if (applicationContext == null) {
            throw new TinyTaskException("找不到 ApplicationContext");
        }
        // 所有Bean
        String[] beanNamesForTypeArray = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanNamesForType : beanNamesForTypeArray) {
            // 跳过@Lazy注解
            if (applicationContext.findAnnotationOnBean(beanNamesForType, Lazy.class) == null) {
                Object bean = applicationContext.getBean(beanNamesForType);
                // 含有@Task注解的方法
                Map<Method, Task> annotatedMethodMap = MethodIntrospector.selectMethods(
                        bean.getClass(),
                        (MethodIntrospector.MetadataLookup<Task>) method ->
                                AnnotatedElementUtils.findMergedAnnotation(method, Task.class));
                annotatedMethodMap.forEach((method, task) -> processTask(beanNamesForType, method, task));
            }
        }
        // 启动
        registrar.afterPropertiesSet();
    }

    /**
     * 处理
     *
     * @param beanName Bean名
     * @param method   Method
     * @param task     Task
     */
    private void processTask(String beanName, Method method, Task task) {
        if (method.getParameters().length != 0) {
            throw new TinyTaskException("方法 " + method + " 不能有参数");
        }

        // 任务名:Bean名.方法名
        String name = beanName + "." + method.getName();

        // Redis和RabbitMQ
        Runnable runnable = () -> {
            if (Boolean.TRUE.equals(rt.setIfAbsent(prefix + ":" + name, timeout))) {
                rabbitTemplate.convertAndSend(prefix, name);
            }
        };

        // cron
        String cron = task.value();
        if (!cron.isEmpty()) {
            TimeZone timeZone;
            String zone = task.zone();
            if (zone.isEmpty()) {
                timeZone = TimeZone.getDefault();
            } else {
                timeZone = TimeZone.getTimeZone(zone);
            }
            registrar.scheduleCronTask(new CronTask(runnable, new CronTrigger(cron, timeZone)));
            return;
        }

        // time unit
        TimeUnit timeUnit = task.timeUnit();

        // initial delay
        long initialDelay = task.initialDelay();
        if (initialDelay > 0) {
            initialDelay = TimeUnit.MILLISECONDS.convert(initialDelay, timeUnit);
        } else if (initialDelay < 0) {
            String initialDelayDuration = task.initialDelayDuration();
            if (initialDelayDuration.isEmpty()) {
                throw new TinyTaskException("方法 " + method + " @Task注解的 initialDelay 或 initialDelayDuration 不合法");
            }
            initialDelay = Duration.parse(initialDelayDuration).toMillis();
        }

        // fixed delay
        long fixedDelay = task.fixedDelay();
        if (fixedDelay > 0) {
            fixedDelay = TimeUnit.MILLISECONDS.convert(fixedDelay, timeUnit);
        } else {
            String fixedDelayDuration = task.fixedDelayDuration();
            if (!fixedDelayDuration.isEmpty()) {
                fixedDelay = Duration.parse(fixedDelayDuration).toMillis();
            }
        }
        if (fixedDelay > 0) {
            registrar.scheduleFixedDelayTask(new FixedDelayTask(runnable, fixedDelay, initialDelay));
            return;
        }

        // fixed rate
        long fixedRate = task.fixedRate();
        if (fixedRate > 0) {
            fixedRate = TimeUnit.MILLISECONDS.convert(fixedRate, timeUnit);
        } else {
            String fixedRateDuration = task.fixedRateDuration();
            if (!fixedRateDuration.isEmpty()) {
                fixedRate = Duration.parse(fixedRateDuration).toMillis();
            }
        }
        if (fixedRate > 0) {
            registrar.scheduleFixedRateTask(new FixedRateTask(runnable, fixedRate, initialDelay));
            return;
        }

        throw new TinyTaskException("方法 " + method + " @Task注解无效");
    }

    /**
     * DisposableBean
     */
    @Override
    public void destroy() {
        registrar.destroy();
    }

}
