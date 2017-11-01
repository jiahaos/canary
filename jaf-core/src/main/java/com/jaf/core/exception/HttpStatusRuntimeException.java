package com.jaf.core.exception;

/**
 * @author jiahao
 * @Package com.jaf.core.exception
 * @Description: http状态异常
 * @date 2017/11/1 13:31
 */
public abstract class HttpStatusRuntimeException extends RuntimeException{

    public abstract int getHttpStatusCode();

    public abstract String getHttpStatusMessage();

    public HttpStatusRuntimeException() {
    }

    public HttpStatusRuntimeException(String message) {
        super(message);
    }

    public HttpStatusRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpStatusRuntimeException(Throwable cause) {
        super(cause);
    }

    public HttpStatusRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
