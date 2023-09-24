package cn.z.tinytask.entity;

import com.alibaba.fastjson2.JSON;

import java.util.Arrays;

/**
 * <h1>RabbitMQ消息</h1>
 *
 * <p>
 * createDate 2023/09/24 15:26:10
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class Msg {

    /**
     * 操作类型
     */
    private OperateType operateType;
    /**
     * 消息类型
     */
    private MsgType msgType;
    /**
     * 对象名
     */
    private String objectName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private String[] paramType;
    /**
     * 参数值
     */
    private String[] paramValue;

    /**
     * 转换为JSON
     */
    public byte[] toJson() {
        return JSON.toJSONBytes(this);
    }

    /**
     * 从JSON解析
     */
    public static Msg parseJson(byte[] bytes) {
        return JSON.parseObject(bytes, Msg.class);
    }

    public OperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(OperateType operateType) {
        this.operateType = operateType;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getParamType() {
        return paramType;
    }

    public void setParamType(String[] paramType) {
        this.paramType = paramType;
    }

    public String[] getParamValue() {
        return paramValue;
    }

    public void setParamValue(String[] paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public String toString() {
        return "Msg{" +
                "operateType=" + operateType +
                ", msgType=" + msgType +
                ", objectName='" + objectName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramType=" + Arrays.toString(paramType) +
                ", paramValue=" + Arrays.toString(paramValue) +
                '}';
    }

}
