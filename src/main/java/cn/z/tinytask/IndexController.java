package cn.z.tinytask;

import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import cn.z.tinytask.entity.Msg;
import cn.z.tinytask.entity.MsgType;
import cn.z.tinytask.entity.OperateType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

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
    public Msg a() throws Exception {
        Msg msg = new Msg();
        msg.setOperateType(OperateType.AUTO);
        msg.setMsgType(MsgType.ONCE);
        Class clazz = this.getClass();
        msg.setObjectName(clazz.getName());
        Method method = clazz.getMethod("test", String.class, int.class, double.class);
        msg.setMethodName(method.getName());
        Parameter[] parameters = method.getParameters();
        String[] paramType = new String[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            paramType[i] = parameters[i].getType().getName();
        }
        msg.setParamType(paramType);
        msg.setParamValue(new String[]{"哈哈哈", "123", "4.56"});
        rabbitTemplate.convertAndSend(tinyTaskProperties.getPrefix(), msg.toJson());
        return msg;
    }

    public void test(String s, int i, double d) {
        System.out.println(s + " " + i + " " + d);
    }

}
