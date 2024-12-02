package cn.z.tinytask;

/**
 * <h1>TinyTask异常类</h1>
 *
 * <p>
 * createDate 2023/08/01 17:40:03
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class TinyTaskException extends RuntimeException {

    /**
     * TinyTask异常
     */
    public TinyTaskException() {
        super();
    }

    /**
     * TinyTask异常
     *
     * @param message 详细信息
     */
    public TinyTaskException(String message) {
        super(message);
    }

    /**
     * TinyTask异常
     *
     * @param message 详细信息
     * @param cause   原因
     */
    public TinyTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * TinyTask异常
     *
     * @param cause 原因
     */
    public TinyTaskException(Throwable cause) {
        super(cause);
    }

    /**
     * TinyTask异常
     *
     * @param message            详细信息
     * @param cause              原因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 堆栈跟踪是否为可写的
     */
    protected TinyTaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
