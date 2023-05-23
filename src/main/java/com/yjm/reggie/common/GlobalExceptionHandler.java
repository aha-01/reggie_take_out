package com.yjm.reggie.common;

/**
 * @author 游锦民
 * @version 1.0
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/*
* 全局异常处理思路:
* 1.创建全局异常处理类
* 2.该类添加@ControllerAdvice注解:用于处理全局控制器异常,数据绑定等
* 3.annotations 参数用于指定需要在其执行全局控制器增强的控制器注解类型,这里需要进行进行异常处理的是EmployeeController层上返回的数据库异常
* 4.创建异常处理方法,类型为自定义的泛型
* 5.判断异常返回的信息是否有姓名重复的关键字,进行处理
* */

/**
 * 全局异常处理作用
 * 在一个统一的地方处理控制器类中抛出的各种类型的异常,并且返回一个合适的响应对象给客户端
 *
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    //该注解表示该方法用于处理参数中类型的异常,当控制器中抛出此类异常时,
    // MVC框架就会调用该方法把异常对象作为方法的参数传递进来
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){ //参数是Java标准库中一个异常类
        log.info(ex.getMessage());

        //判断异常里面是否含有该关键字
        if (ex.getMessage().contains("Duplicate entry")){
            //进行空格分割
            //把重复的姓名抽取出来返回客户端
            String[] split = ex.getMessage().split(" ");
            //由异常信息可以看出第三个就是重复的用户名
            String msg = split[2] + "名字已存在";
            return R.error(msg);
        }

        return R.error("未知错误");

    }

    /**
     * 菜品异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){ //参数是Java标准库中一个异常类
        log.error(ex.getMessage());

        //R类作为响应对象,并且将异常信息作为R对象的恶属性传递给页面
        return R.error(ex.getMessage());

    }

}
