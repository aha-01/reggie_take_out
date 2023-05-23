package com.yjm.reggie.config;

import com.yjm.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @author 游锦民
 * @version 1.0
 */
@Slf4j
@Configuration //spring配置类注解
public class WebMvcConfig extends WebMvcConfigurationSupport {//继承该类用于配置springMvc的相关配置
    /**
     * 静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {//该方法用于添加静态资源的映射
        //addResourceHandler()指定访问路径的前缀
        //addResourceLocations()指定静态资源文件的路径,即在 classpath 下的 backend 目录中寻找静态资源文件。
        log.info("静态资源映射~");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }


    /**
     * 扩展mvc框架的消息转换器
     * @param converters
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("自定义消息转化器 被调用!");
        //创建消息转换对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器,底层使用jackson将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将上面的消息转换器对象追加到mvc框架的转换器集合中的第一个位置,有限采用下标为 0 位置的消息转换器
        converters.add(0,messageConverter);

    }
}
