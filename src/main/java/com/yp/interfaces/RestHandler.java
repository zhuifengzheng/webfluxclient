package com.yp.interfaces;

import com.yp.beans.MethodInfo;
import com.yp.beans.ServerInfo;

import java.lang.reflect.Method;

/**
 * @author fengzheng
 * @create 2018-09-15 11:16
 *  rest请求调用handler
 **/
public interface RestHandler {
    /**
     * 初始化服务器信息
     * @param serverInfo
     */
    void init(ServerInfo serverInfo);

    /**
     * 初始化rest请求，返回接口
     * @param methodInfo
     * @return
     */
    Object invokeRest(MethodInfo methodInfo);
}
