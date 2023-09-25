package cn.z.tinytask.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

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
@Configuration
public class TaskAnnotationProcessor implements BeanPostProcessor {

    /**
     * 日志实例
     */
    private static final Logger log = LoggerFactory.getLogger(TaskAnnotationProcessor.class);

    /**
     * 轻量级集群任务存储
     */
    private final TaskStorage taskStorage;

    /**
     * 构造函数(自动注入)
     *
     * @param taskStorage TaskStorage
     */
    public TaskAnnotationProcessor(TaskStorage taskStorage) {
        this.taskStorage = taskStorage;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

}
