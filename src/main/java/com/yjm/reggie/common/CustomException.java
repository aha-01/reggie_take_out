package com.yjm.reggie.common;

/**
 * @author 游锦民
 * @version 1.0
 */

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message); //调用RuntimeException类的构造器,传递一个字符串参数为异常信息
    }
}
