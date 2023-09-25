package cn.z.tinytask;

import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

/**
 * <h1>IndexController</h1>
 *
 * <p>
 * createDate 2023/09/24 15:24:36
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@RestController
public class IndexController {

    /**
     * RabbitMQ模板
     */
    private final RabbitTemplate rabbitTemplate;
    /**
     * 轻量级集群任务配置属性
     */
    private final TinyTaskProperties tinyTaskProperties;

    public IndexController(RabbitTemplate rabbitTemplate, TinyTaskProperties tinyTaskProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.tinyTaskProperties = tinyTaskProperties;
    }

    @GetMapping("a")
    public String a() throws Exception {
        Class clazz = this.getClass();
        Method method = clazz.getMethod("test");
        String msg = clazz.getName() + "." + method.getName();
        rabbitTemplate.convertAndSend(tinyTaskProperties.getPrefix(), msg);
        return msg;
    }

    public void test() {
        System.out.println("12222");
    }

}
