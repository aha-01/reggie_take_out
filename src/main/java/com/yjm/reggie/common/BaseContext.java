package com.yjm.reggie.common;

/**
 * @author 游锦民
 * @version 1.0
 */
//创建一个公共的线程副本变量类,用于存储当前登录用户的id
public class BaseContext {

    //创建ThreadLocat对象,存储Long类型的线程副本变量
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //创建设置值的方法
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    //获取值的方法
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
