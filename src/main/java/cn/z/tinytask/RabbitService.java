package cn.z.tinytask;

import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
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
@Service
public class RabbitService {

    /**
     * 日志实例
     */
    private static final Logger log = LoggerFactory.getLogger(RabbitService.class);

    /**
     * 构造函数(自动注入)
     *
     * @param tinyTaskProperties TinyTaskProperties
     * @param factory            ConnectionFactory
     */
    public RabbitService(TinyTaskProperties tinyTaskProperties, ConnectionFactory factory, IndexController indexController) throws Exception {
        try (Connection connection = factory.createConnection()) {
            Map<String, Object> arguments = new HashMap<>(1);
            // 队列消息过期时间
            arguments.put("x-message-ttl", tinyTaskProperties.getTimeout() * 1000);
            try (Channel channel = connection.createChannel(false)) {
                // 创建队列
                channel.queueDeclare(tinyTaskProperties.getPrefix(), true, true, true, arguments);
                // 监听消息
                channel.basicConsume(tinyTaskProperties.getPrefix(), true, new DefaultConsumer(channel) {

                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                        try {
                            String msg = new String(body, StandardCharsets.UTF_8);
                            int index = msg.lastIndexOf('.');
                            String className = msg.substring(0, index);
                            String methodName = msg.substring(index + 1);
                            Method method = indexController.getClass().getMethod(methodName);
                            method.invoke(indexController);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }
    }

}
