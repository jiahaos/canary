package com.jaf.core.exception;

/**
 * @author jiahao
 * @Package com.jaf.core.exception
 * @Description: 业务http异常
 * @date 2017/11/1 13:59
 */
public class BizHttpStatusRuntimeException extends HttpStatusRuntimeException {

    private int httpStatusCode;
    private String httpStatusMessage;

    public BizHttpStatusRuntimeException(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public BizHttpStatusRuntimeException(int httpStatusCode, String httpStatusMessage) {
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
