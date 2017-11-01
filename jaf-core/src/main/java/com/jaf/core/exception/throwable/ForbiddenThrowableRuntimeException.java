package com.jaf.core.exception.throwable;

import com.jaf.core.exception.ApErrorCode;

/**
 * @author jiahao
 * @Package com.jaf.core.exception.throwable
 * @Description: 禁用型异常类
 * @date 2017/11/1 11:58
 */
public class ForbiddenThrowableRuntimeException extends ThrowableRuntimeException {

    private int code;
    private String throwableMessage;

    public ForbiddenThrowableRuntimeException() {
        this.code = ApErrorCode.ACCESS_FORBIDDEN;
        this.throwableMessage = "access Forbidden!";
    };

    public ForbiddenThrowableRuntimeException(String throwableMessage) {
        this.code = ApErrorCode.ACCESS_FORBIDDEN;
        this.throwableMessage = throwableMessage;
    }

    @Override
    public int getHttpStatusCode() {
        return this.code;
    }

    @Override
    public String getHttpStatusMessage() {
        return this.throwableMessage;
    }
}
