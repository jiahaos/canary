package org.jaf.context.http.error;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.jaf.core.exception.ApErrorCode;
import com.jaf.core.exception.throwable.BizThrowableRuntimeException;
import com.jaf.core.exception.throwable.ForbiddenThrowableRuntimeException;
import com.jaf.core.exception.throwable.ThrowableRuntimeException;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jiahao
 * @Package org.jaf.context.http.error
 * @Description: response异常拦截重写封装
 * @date 2017/11/2 9:42
 */
@Slf4j
@ControllerAdvice
public class HttpErrorAdvice {

    private static final String CODE = "code";
    private static final String MESSAGE = "message";

    @ExceptionHandler
    public void rewriteAdviceException(Throwable throwable, HttpServletResponse response) throws Exception{

        ServletOutputStream servletOutputStream = response.getOutputStream();
        JSONObject body = new JSONObject();
        if(throwable instanceof BizThrowableRuntimeException) {
            response.setStatus(510);
            ThrowableRuntimeException throwableExp = (BizThrowableRuntimeException) throwable;
            body.put(CODE, throwableExp.getHttpStatusCode());
            body.put(MESSAGE, throwableExp.getHttpStatusMessage());
        }else if(throwable instanceof ForbiddenThrowableRuntimeException) {
            response.setStatus(403);
            ThrowableRuntimeException forbidden = (BizThrowableRuntimeException) throwable;
            body.put(CODE, forbidden.getHttpStatusCode());
            body.put(MESSAGE, forbidden.getHttpStatusMessage());
        }else {
            response.setStatus(500);
            body.put(CODE, ApErrorCode.UNKNOW_ERROR);
            body.put(MESSAGE, Throwables.getStackTraceAsString(throwable));
            log.error("Biz Not Catch Throwable.", throwable);
        }
        servletOutputStream.write(body.toJSONString().getBytes("UTF-8"));
        servletOutputStream.flush();
        servletOutputStream.close();
    }

}
