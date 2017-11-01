package com.jaf.core.exception;

/**
 * @author jiahao
 * @Package com.jaf.core.exception.throwable
 * @Description: httpStatus禁用型异常
 * @date 2017/11/1 13:25
 */
public class ForbiddenHttpStatusRuntimeException extends HttpStatusRuntimeException{

    private int httpStatusCode;
    private String httpStatusMessage;

    public ForbiddenHttpStatusRuntimeException(int httpStatusCode) {
       this.httpStatusCode = httpStatusCode;
    }

    public ForbiddenHttpStatusRuntimeException(int httpStatusCode, String httpStatusMessage) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatusMessage = httpStatusMessage;
    }

    @Override
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    @Override
    public String getHttpStatusMessage() {
        return this.httpStatusMessage;
    }
}
