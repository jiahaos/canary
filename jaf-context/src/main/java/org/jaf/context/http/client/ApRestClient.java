package org.jaf.context.http.client;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author jiahao
 * @Package org.jaf.context.http.client
 * @Description: restCient封装类
 * @date 2017/11/1 15:47
 */
@Slf4j
@Component
public class ApRestClient {

    private RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    private void init() {
        //设置超时地址
        //https://stackoverflow.com/questions/13837012/spring-resttemplate-timeout
        SimpleClientHttpRequestFactory rf =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        rf.setReadTimeout(10 * 1000);
        rf.setConnectTimeout(3 * 1000);
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//        HttpComponentsClientHttpRequestFactory httpRequestFactory = restTemplate.getRequestFactory();
//        httpRequestFactory.setConnectionRequestTimeout(...);
//        httpRequestFactory.setConnectTimeout(...);
//        httpRequestFactory.setReadTimeout(...);
//
//        return new RestTemplate(httpRequestFactory);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /**
     *  get json by http GET and turns it to object
     * @return
     * @throws RestClientException
     */
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException{
        try {
            return  restTemplate.getForObject(url, responseType, uriVariables);
        }catch (Exception e) {
            throw new RestClientException("url connection failed，url=" + url);
        }
    }

    /**
     *  post json by http POST and turns it to object
     * @return
     * @throws RestClientException
     */
    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException{
        try {
            return  restTemplate.postForObject(url, request, responseType, uriVariables);
        }catch (Exception e) {
            throw new RestClientException("url connection failed，url=" + url);
        }
    }

    /**
     *  post json by http GET and turns it to ListObject
     * @return
     * @throws RestClientException
     */
    public <T> List<T> getForListObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException{
        try {
            String result = restTemplate.getForObject(url, String.class, uriVariables);
            return  JSON.parseArray(result, responseType);
        }catch (Exception e) {
            throw new RestClientException("url connection failed，url=" + url);
        }
    }


    /**
     *  post json by http POST and turns it to ListObject
     * @return
     * @throws RestClientException
     */
    public <T> List<T> postForListObject(String url, Object request, Class<T> responseType, Object... uriVariables) throws RestClientException{
        try {
            String result = restTemplate.postForObject(url, request, String.class, uriVariables);
            return  JSON.parseArray(result, responseType);
        }catch (Exception e) {
            throw new RestClientException("url connection failed，url=" + url);
        }
    }

    /**
     *  post json by http POST with Header and turns it to object
     * @return
     * @throws RestClientException
     */
    public <T> T postForObjectWithHeader(String url, Object request, Map<String, String> headerParameters, Class<T> responseType) throws RestClientException{
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            if(headerParameters != null) {
                headerParameters.keySet().forEach(
                        p -> {
                              httpHeaders.add(p, headerParameters.get(p));
                        }
                );
            }
            ResponseEntity<T> entity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(request, httpHeaders), responseType);
            return entity.getBody();
        }catch (Exception e) {
            throw new RestClientException("url connection failed，url=" + url);
        }
    }
}
