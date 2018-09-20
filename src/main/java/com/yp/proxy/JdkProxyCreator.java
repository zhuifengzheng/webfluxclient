package com.yp.proxy;

import com.yp.beans.MethodInfo;
import com.yp.beans.ServerInfo;
import com.yp.handlers.WebClientRestHandler;
import com.yp.interfaces.IUserApi;
import com.yp.interfaces.ProxyCreator;
import com.yp.interfaces.RestHandler;
import com.yp.service.ApiServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author fengzheng
 * @create 2018-09-15 10:29
 * @desc
 **/
@Component
@Slf4j
public class JdkProxyCreator implements ProxyCreator {

    @Override
    public Object createProxy(Class<?> type) {
        log.info("type : {}", type);
        //得到ApiServer的服务地址信息url
        ServerInfo serverInfo = extractServerInfo(type);
        log.info("serverInfo : {}", serverInfo);
        //给每个代理类一个实现调用
        RestHandler restHandler = new WebClientRestHandler();

        restHandler.init(serverInfo);
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{type}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //根据方法和参数得到调用信息
                        MethodInfo methodInfo = extractMethodInfo(method, args);
                        log.info("methodInfo : {}", methodInfo);
                        return restHandler.invokeRest(methodInfo);
                    }
                });
    }


    private MethodInfo extractMethodInfo(Method method, Object[] args) {
        MethodInfo methodInfo = new MethodInfo();
        extractRequestUrlAndMethod(method, methodInfo);

        extractRequestParamAndBody(method, args, methodInfo);

        extractReturnInfo(method, methodInfo);
        return methodInfo;
    }

    /**\
     * 得到方法返回信息
     * @param method
     * @param methodInfo
     */
    private void extractReturnInfo(Method method, MethodInfo methodInfo) {
        //isAssignableFrom判断类型是否是某个类型的子类
        //instanceof 判断实例是否是某个类型的子类
        boolean assignableFrom = method.getReturnType().isAssignableFrom(Flux.class);
        methodInfo.setReturnFlux(assignableFrom);
        Class<?> elementType = extractElementType(method.getGenericReturnType());
        methodInfo.setReturnElementType(elementType);
    }

    /**
     * 得到返回的实际类型
     * @param genericReturnType
     * @return
     */
    private Class<?> extractElementType(Type genericReturnType) {
        Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
        return (Class<?>) actualTypeArguments[0];
    }

    /**
     * 得到请求的参数和url
     * @param method
     * @param methodInfo
     * @return
     */
    private void extractRequestUrlAndMethod(Method method, MethodInfo methodInfo) {
        //得到url和请求类型
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation:annotations) {
            if(annotation instanceof GetMapping){
                GetMapping get = (GetMapping) annotation;
                methodInfo.setMethod(HttpMethod.GET);
                methodInfo.setUrl(get.value()[0]);
            }
            else if(annotation instanceof PostMapping){
                PostMapping get = (PostMapping) annotation;
                methodInfo.setMethod(HttpMethod.POST);
                methodInfo.setUrl(get.value()[0]);
            }else if(annotation instanceof DeleteMapping){
                DeleteMapping get = (DeleteMapping) annotation;
                methodInfo.setMethod(HttpMethod.DELETE);
                methodInfo.setUrl(get.value()[0]);
            }

        }
    }

    /**
     * 得到请求的参数和body
     * @param method
     * @param args
     * @param methodInfo
     */
    private void extractRequestParamAndBody(Method method, Object[] args, MethodInfo methodInfo) {
        Map<String, Object> param = new LinkedHashMap<>();
        //得到调用的参数和body
        Parameter[] parameters = method.getParameters();

        for(int i=0; i<parameters.length; i++){
            PathVariable annotation = parameters[i].getAnnotation(PathVariable.class);
            if(annotation != null){
                param.put(annotation.value(), args[i]);
            }
            RequestBody annotationBody = parameters[i].getAnnotation(RequestBody.class);
            if(annotationBody != null){
                methodInfo.setBody((Mono<?>) args[i]);
                //请求对象的实体类型
                methodInfo.setBodyElementType(extractElementType(parameters[i].getParameterizedType()));
            }
        }
        methodInfo.setParams(param);
    }

    private ServerInfo extractServerInfo(Class<?> type) {
        ServerInfo serverInfo = new ServerInfo();
        ApiServer annotation = type.getAnnotation(ApiServer.class);
        String url = annotation.value();
        serverInfo.setUrl(url);
        return serverInfo;
    }
}