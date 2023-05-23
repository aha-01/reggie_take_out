package com.yjm.reggie.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置MP的分页插件
 */
@Configuration //配置类
public class MybatisPlusConfig {

    //创建分页插件思路
    //1.通过MPplus自带分页插件功能通过拦截器调用进来

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        //创建拦截器对象
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //并在该拦截器对象分钟添加分页对象,这是MP内置的分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //在MYplus执行SQL语句时,就会自动调用这个分页插件进行分页操作
        return mybatisPlusInterceptor;
    }
}
