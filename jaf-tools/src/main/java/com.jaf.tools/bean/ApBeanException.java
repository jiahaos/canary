package com.jaf.tools.bean;

public class ApBeanException extends RuntimeException {

    public ApBeanException() {
        super();
    }

    public ApBeanException(final String msg) {
        super("[ARSF.Common]：" + msg);
    }

    public ApBeanException(final String msg, final Throwable cause) {
        super("[ARSF.Common]：" + msg, cause);
    }

}
