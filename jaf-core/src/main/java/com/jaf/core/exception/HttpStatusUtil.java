package com.jaf.core.exception;

/**
 * @author jiahao
 * @Package com.jaf.core.exception
 * @Description: 运行时异常封装工具
 * @date 2017/11/1 13:39
 */
public class HttpStatusUtil {

    public static void returnHttpCode(int code, String message) {
        throw new BizHttpStatusRuntimeException(code, message);
    }

    public static void returnHttpCode(String message) {
        throw new BizHttpStatusRuntimeException(ApErrorCode.UNKNOW_ERROR, message);
    }

}
