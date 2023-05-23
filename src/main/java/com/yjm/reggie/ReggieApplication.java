package com.yjm.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author 游锦民
 * @version 1.0
 */
//打印日志方便调试
@Slf4j
//编写启动类
@SpringBootApplication
@ServletComponentScan //扫描过滤器注解
@EnableTransactionManagement

public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动successful....");
    }
}
