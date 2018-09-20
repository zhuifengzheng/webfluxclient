package com.yp.handlers;

import com.yp.beans.MethodInfo;
import com.yp.beans.ServerInfo;
import com.yp.interfaces.RestHandler;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.Method;

/**
 * @author fengzheng
 * @create 2018-09-15 11:34
 * @desc
 **/
public class WebClientRestHandler implements RestHandler {

    private WebClient webClient;

    /**
     * 初始化webClient
     * @param serverInfo
     */
    @Override
    public void init(ServerInfo serverInfo) {
        this.webClient = WebClient.create(serverInfo.getUrl());
    }

    /**
     * 处理rest请求
     * @param methodInfo
     * @return
     */
    @Override
    public Object invokeRest(MethodInfo methodInfo) {
        Object result = null;
        WebClient.RequestBodySpec requset = this.webClient
                .method(methodInfo.getMethod())//请求方法
                .uri(methodInfo.getUrl(), methodInfo.getParams())//请求url和参数
                .accept(MediaType.APPLICATION_JSON);//请求接收类型
        WebClient.ResponseSpec retrieve = null;
        if(methodInfo.getBody() != null){
            retrieve = requset.body(methodInfo.getBody(), methodInfo.getBodyElementType()).retrieve();//发出请求;
        }else{
            retrieve = requset.retrieve();
        }

        if(methodInfo.isReturnFlux()){
            result = retrieve.bodyToFlux(methodInfo.getReturnElementType());
        }else{
            result = retrieve.bodyToMono(methodInfo.getReturnElementType());
        }

        return result;
    }
}