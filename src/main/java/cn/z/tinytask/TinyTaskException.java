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
     * @param message 信息
     */
    public TinyTaskException(String message) {
        super(message);
    }

}
