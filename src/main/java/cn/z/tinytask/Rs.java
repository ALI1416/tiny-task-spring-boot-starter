package cn.z.tinytask;

import cn.z.tinytask.annotation.TaskAnnotationProcessor;
import cn.z.tinytask.autoconfigure.TinyTaskRabbitProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>RabbitMQ服务</h1>
 *
 * <p>
 * createDate 2023/09/23 17:21:32
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class Rs {

    /**
     * 日志实例
     */
    private static final Logger log = LoggerFactory.getLogger(Rs.class);

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTaskRabbitProperties TinyTaskRabbitProperties
     * @param factory                  ConnectionFactory
     */
    public Rs(TinyTaskRabbitProperties tinyTaskRabbitProperties, ConnectionFactory factory) throws Exception {
        try (Connection connection = factory.createConnection()) {
            Map<String, Object> arguments = new HashMap<>(1);
            // 队列消息过期时间
            arguments.put("x-message-ttl", tinyTaskRabbitProperties.getTimeout() * 1000);
            try (Channel channel = connection.createChannel(false)) {
                // 创建队列
                channel.queueDeclare(tinyTaskRabbitProperties.getPrefix(), true, false, true, arguments);
                // 监听消息
                channel.basicConsume(tinyTaskRabbitProperties.getPrefix(), true, new DefaultConsumer(channel) {

                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                        try {
                            // 任务名:Bean名.方法名
                            String msg = new String(body, StandardCharsets.UTF_8);
                            int index = msg.lastIndexOf('.');
                            Object bean = TaskAnnotationProcessor.getBean(msg.substring(0, index));
                            bean.getClass().getMethod(msg.substring(index + 1)).invoke(bean);
                        } catch (Exception e) {
                            log.error("任务执行异常！", e);
                        }
                    }

                });
            }
        }
    }

}
