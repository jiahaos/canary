package org.jaf.context.http.client;

/**
 * @author jiahao
 * @Package org.jaf.context.http.client
 * @Description: restClient异常封装类
 * @date 2017/11/1 16:58
 */
public class RestClientException extends Exception {

    public RestClientException() {
    }

    public RestClientException(String message) {
        super(message);
    }
}
