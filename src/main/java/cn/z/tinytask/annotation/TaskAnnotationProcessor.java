package cn.z.tinytask.annotation;

import cn.z.tinytask.Rt;
import cn.z.tinytask.TinyTaskException;
import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import cn.z.tinytask.autoconfigure.TinyTaskRabbitProperties;
import cn.z.tinytask.autoconfigure.TinyTaskRedisProperties;
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
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executors;

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
     * 轻量级集群任务配置属性
     */
    private final TinyTaskProperties tinyTaskProperties;
    /**
     * 轻量级集群任务Redis配置属性
     */
    private final TinyTaskRedisProperties tinyTaskRedisProperties;
    /**
     * 轻量级集群任务RabbitMQ配置属性
     */
    private final TinyTaskRabbitProperties tinyTaskRabbitProperties;
    /**
     * Redis模板
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
     * @param tinyTaskProperties       TinyTaskProperties
     * @param tinyTaskRedisProperties  TinyTaskRedisProperties
     * @param tinyTaskRabbitProperties TinyTaskRabbitProperties
     * @param rt                       Rt
     * @param rabbitTemplate           RabbitTemplate
     */
    public TaskAnnotationProcessor(TinyTaskProperties tinyTaskProperties, TinyTaskRedisProperties tinyTaskRedisProperties, TinyTaskRabbitProperties tinyTaskRabbitProperties, Rt rt, RabbitTemplate rabbitTemplate) {
        this.tinyTaskProperties = tinyTaskProperties;
        this.tinyTaskRedisProperties = tinyTaskRedisProperties;
        this.tinyTaskRabbitProperties = tinyTaskRabbitProperties;
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
        // 设置线程池数量
        registrar.setScheduler(Executors.newScheduledThreadPool(tinyTaskProperties.getThreadPoolSize()));
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
        String cron = task.value();
        if (!cron.isEmpty()) {
            // 时区
            TimeZone timeZone;
            String zone = task.zone();
            if (zone.isEmpty()) {
                timeZone = TimeZone.getDefault();
            } else {
                timeZone = TimeZone.getTimeZone(zone);
            }
            // 任务名:Bean名.方法名
            String name = beanName + "." + method.getName();
            // Redis和RabbitMQ
            Runnable runnable = () -> {
                if (Boolean.TRUE.equals(rt.setIfAbsent(tinyTaskRedisProperties.getPrefix() + ":" + name, tinyTaskRedisProperties.getTimeout()))) {
                    rabbitTemplate.convertAndSend(tinyTaskRabbitProperties.getPrefix(), name);
                }
            };
            // 添加cron任务
            registrar.addCronTask(new CronTask(runnable, new CronTrigger(cron, timeZone)));
        } else {
            throw new TinyTaskException("方法 " + method + " @Task注解无效");
        }
    }

    /**
     * DisposableBean
     */
    @Override
    public void destroy() {
        registrar.destroy();
    }

}
