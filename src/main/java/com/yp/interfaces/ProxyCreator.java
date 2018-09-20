package com.yp.interfaces;

/**
 * @author fengzheng
 * @create 2018-09-15 10:26
 * @desc
 **/
public interface ProxyCreator {
    Object createProxy(Class<?> type);
}