package com.jaf.core.exception.throwable;

/**
 * @author jiahao
 * @Package com.jaf.core.exception.throwable
 * @Description: 可抛出运行时异常
 * @date 2017/11/1 13:14
 */
public abstract class ThrowableRuntimeException extends RuntimeException {

    public abstract int getHttpStatusCode();

    public abstract String getHttpStatusMessage();

    public ThrowableRuntimeException() {
    }

    public ThrowableRuntimeException(String message) {
        super(message);
    }

    public ThrowableRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThrowableRuntimeException(Throwable cause) {
        super(cause);
    }

    public ThrowableRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
