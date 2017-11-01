package com.jaf.core.exception.throwable;

/**
 * @author jiahao
 * @Package com.jaf.core.exception.throwable
 * @Description: 业务异常类
 * @date 2017/11/1 11:58
 */
public class BizThrowableRuntimeException extends ThrowableRuntimeException {

    private int code;
    private String message;

    public BizThrowableRuntimeException() {};

    public BizThrowableRuntimeException(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @Override
    public int getHttpStatusCode() {
        return this.code;
    }

    @Override
    public String getHttpStatusMessage() {
        return this.message;
    }
}
