package com.jaf.tools.security;

public class ApSecurityRuntimeException extends RuntimeException {

    public ApSecurityRuntimeException() {
    }

    public ApSecurityRuntimeException(String message) {
        super(message);
    }

    public ApSecurityRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApSecurityRuntimeException(Throwable cause) {
        super(cause);
    }

    public ApSecurityRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
