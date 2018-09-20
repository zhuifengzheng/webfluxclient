package com.yp.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author fengzheng
 * @create 2018-09-15 10:54
 * @desc
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MethodInfo {

    /**
     * 请求的uri
     */
    private String url;

    /**
     * 请求的方法
     */
    private HttpMethod method;

    /**
     * 请求的参数
     */
    private Map<String, Object> params;

    /**
     * 请求的body
     */
    private Mono body;

    /**
     * 请求的body类型
     */
    private Class<?> bodyElementType;

    /**
     * 判断返回类型是flux还是mono类型
     */
    private boolean isReturnFlux;

    /**
     * 判断返回类型
     */
    private Class<?> returnElementType;
}