package com.yjm.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.yjm.reggie.common.BaseContext;
import com.yjm.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*") //该注解指定过滤器名称和过滤器要拦截的URL请求路径匹配模式为/*
//拦截器的原理是基于AOP的,所以拦截器必须实现Filter接口,该接口的方法在请求的生命周期中会被自动调用,该接口是在javax.servlet包中定义的
public class LoginCheckFilter implements Filter {

    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //获取当前线程的id
//        long id = Thread.currentThread().getId();
//        log.info("线程id:{}",id);
        //强制转换之后能够获取HTTP请求的详细信息
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        //1.获取本次请求的URL
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "common/**",
            //放行验证码的发送页面和验证码的发送请求
                "/user/sendMsg", //移动端发送短信地址
                "/user/login"    //移动端登录

        };

        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.如果不需要处理,则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //4.1判断登录状态,如果已登录,则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录,用户id为{}", request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            //设置当前用户的id,用于公共字段自动填充
            BaseContext.setCurrentId(empId);
            log.info("测试测试前线程的副本变量当前用户的id为：{}",BaseContext.getCurrentId());
            filterChain.doFilter(request, response);
            return;
        }


        //4-2、判断前端用户登录状态(session是否含有user的登录信息)，如果已经登录，则直接放行
        if (request.getSession().getAttribute("user") != null) {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            //获取当前用户的id，用于公共字段自动填充
            Long userId = (Long) request.getSession().getAttribute("user");

            //下面这句话的意思是：将当前用户的id存储到当前线程的副本变量中,这样在后面的代码中就可以获取到当前用户的id
            BaseContext.setCurrentId(userId);

            //
            filterChain.doFilter(request,response);
            return;
        }


        //5.如果未登录则返回未登录结果,通过输出流方式向客户端页面响应数据
        //将结果转换为JSON字符串告诉给前端,前端的响应拦截器就捕获到未登录的信息,然后就跳转到登录页面
        //目的保证用户必须登录才能访问到后台页面
        log.info("用户未登录");

        //设置响应的数据类型为JSON
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;
//        //Slf4j日志框架记录请求的URI信息，这里使用了{}占位符，可以在占位符中填写具体的参数值。
//        log.info("拦截请求:{}", request.getRequestURI());


//        //调用doFilter()方法将请求传递给下一个过滤器Filter或者Web容器处理,如果没有下一过滤器,那么请求就会直接被Web容器处理
//        filterChain.doFilter(request, response);
    }

    /**
     * 路径匹配,检查本次请求是否需要放行
     *
     * @param urls
     * @param requerstURI
     * @return
     */
    public boolean check(String[] urls, String requerstURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requerstURI);
            if (match) {
                //匹配了就返回true
                return true;
            }
        }
        return false;
    }

}
