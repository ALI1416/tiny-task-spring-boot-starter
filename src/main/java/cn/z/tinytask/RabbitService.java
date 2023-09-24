package cn.z.tinytask;

import cn.z.tinytask.autoconfigure.TinyTaskProperties;
import cn.z.tinytask.entity.Msg;
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
                            Msg msg = Msg.parseJson(body);
                            String[] paramType = msg.getParamType();
                            Class[] paramTypeFix = new Class[paramType.length];
                            String[] paramValue = msg.getParamValue();
                            Object[] paramValueFix = new Object[paramTypeFix.length];
                            for (int i = 0; i < paramType.length; i++) {
                                paramTypeFix[i] = getClassFix(paramType[i]);
                                paramValueFix[i] = castString(paramTypeFix[i], paramValue[i]);
                            }
                            Method method = indexController.getClass().getMethod(msg.getMethodName(), paramTypeFix);
                            method.invoke(indexController, paramValueFix);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }
    }

    /**
     * 获取修正类(增加9种基本类型)
     *
     * @param className 类名
     * @return Class
     */
    public static Class getClassFix(String className) {
        switch (className) {
            case "void": {
                return void.class;
            }
            case "boolean": {
                return boolean.class;
            }
            case "byte": {
                return byte.class;
            }
            case "char": {
                return char.class;
            }
            case "short": {
                return short.class;
            }
            case "int": {
                return int.class;
            }
            case "long": {
                return long.class;
            }
            case "float": {
                return float.class;
            }
            case "double": {
                return double.class;
            }
            default: {
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new TinyTaskException("找不到类 " + className);
                }
            }
        }
    }

    /**
     * String转换
     *
     * <p>
     * <code>String</code>类型：返回本身<br>
     * <code>Void</code>类型：返回<code>null</code><br>
     * 8种基本类型的包装类型：当参数<code>String</code>的长度为<code>0</code>时返回<code>null</code><br>
     * 8种基本类型及包装类型：先转为<code>UTF-8</code>字符串，再转为对应类型<br>
     * 自定义类型：调用它的入参为<code>String</code>的构造函数并返回
     * </p>
     *
     * @param clazz  Class
     * @param string String
     * @return Object
     */
    private static Object castString(Class<?> clazz, String string) {
        switch (clazz.getTypeName()) {
            case "java.lang.String": {
                return string;
            }
            case "java.lang.Void": {
                return null;
            }
            case "boolean": {
                return Boolean.parseBoolean(string);
            }
            case "java.lang.Boolean": {
                if (string.length() == 0) {
                    return null;
                }
                return Boolean.parseBoolean(string);
            }
            case "byte": {
                return Byte.parseByte(string);
            }
            case "java.lang.Byte": {
                if (string.length() == 0) {
                    return null;
                }
                return Byte.parseByte(string);
            }
            case "char": {
                if (string.length() == 1) {
                    return string.charAt(0);
                }
                throw new IndexOutOfBoundsException("char类型只能接收 1 个字符，当前为 " + string.length() + " 字符");
            }
            case "java.lang.Character": {
                if (string.length() == 0) {
                    return null;
                }
                if (string.length() == 1) {
                    return string.charAt(0);
                }
                throw new IndexOutOfBoundsException("Character类型只能接收 1 个字符，当前为 " + string.length() + " 字符");
            }
            case "short": {
                return Short.parseShort(string);
            }
            case "java.lang.Short": {
                if (string.length() == 0) {
                    return null;
                }
                return Short.parseShort(string);
            }
            case "int": {
                return Integer.parseInt(string);
            }
            case "java.lang.Integer": {
                if (string.length() == 0) {
                    return null;
                }
                return Integer.parseInt(string);
            }
            case "long": {
                return Long.parseLong(string);
            }
            case "java.lang.Long": {
                if (string.length() == 0) {
                    return null;
                }
                return Long.parseLong(string);
            }
            case "float": {
                return Float.parseFloat(string);
            }
            case "java.lang.Float": {
                if (string.length() == 0) {
                    return null;
                }
                return Float.parseFloat(string);
            }
            case "double": {
                return Double.parseDouble(string);
            }
            case "java.lang.Double": {
                if (string.length() == 0) {
                    return null;
                }
                return Double.parseDouble(string);
            }
            default: {
                try {
                    return clazz.getConstructor(String.class).newInstance(string);
                } catch (Exception e) {
                    throw new TinyTaskException("无法使用值 " + string + " 实例化类 " + clazz + " 的入参为 String 的构造方法");
                }
            }
        }
    }

}
