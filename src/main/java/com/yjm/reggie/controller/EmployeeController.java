package com.yjm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjm.reggie.common.R;
import com.yjm.reggie.entity.Employee;
import com.yjm.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

/**
 * @author 游锦民
 * @version 1.0
 */
@Slf4j
@RestController  //@RestController注解来标识它是一个RESTful API控制器,用于接收客户端发送的HTTP请求,并根据请求的内容执行相应的操作,并将操作结果返回客户端
@RequestMapping("/employee") //将HTTP请求映射到特定的控制器上,实现对不同类型的请求进行处理
//综上,@RestController 接收请求,@RequestMapping("/employee") 判断该请求的URL路径是不是/employee,是则调用相对应的处理方法来处理请求
public class EmployeeController {

    @Autowired //自动注入一个EmployeeService类型的bean
    private EmployeeService employeeService;


    /*
    思路:
    * 1.编写能接收添加员工请求的方法save(),处理HHTP POST请求添加@mapping注解
    * 2.该方法的参数1就是让控制器能够访问和操作HTTP请求和响应,@RequestBody 用于解析json数据转换为employee实体类
    * 3.设置初始化密码,并进行MD5加密,添加日志信息用于测试
    * 4.给实体类中的属性赋值,例如创建时间和修改时间
      5.获取当前登录用户的id
      6.返回封装的成功的信息
    * */

    /**
     * 新增员工
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        //日志
        log.info("新增员工,员工信息:{}", employee.toString());

        //设置员工初始密码123456,进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //设置创建和修改时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //获取当前登录用户的id
        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        //把实体类注入到数据库当中
        employeeService.save(employee);

        return R.success("新增员工成功");


    }


    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page= {},pageSize= {},name= {}", page, pageSize, name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        //构造条件构造器
        //MB中的一个构建SQL查询条件的查询构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();

        //添加过滤条件
        //使用Lambada表达式构建查询条件,使用like方法按名称过滤员工
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);

        //添加排序条件, 根据用户的时间升序分页展示
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        //换行查询
        employeeService.page(pageInfo, queryWrapper);

        //执行查询
        return R.success(pageInfo);


    }

    /**
     * 创建员工
     *
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        long id = Thread.currentThread().getId();
        log.info("线程id:{}",id);
        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    /**
     * 员工信息编辑
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        log.info("根据id查询员工信息: id ={}" + String.valueOf(id));
        Employee employee = employeeService.getById(id);
        if (employee != null) {

            return R.success(employee);
        }
        return R.error("没有查到对应员工信息");
    }


    /**
     * 登录功能
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    //HttpServletRequest request提供访问HTTP特定信息的功能,使控制器方法能够访问和操作HTTP请求和响应
    //@RequestBody的作用表示控制器方法从请求体中解析一个JSON或XML数据,并将其转换为'Employee'类型的Java对象
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //1.将页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //参数1是数据库的用户名,参数2是上面类传进来的用户名
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        //使用getOne方法证明数据库的用户名字段使唯一的
        Employee emp = employeeService.getOne(queryWrapper);


        //3,如果没有查到就返回登录失败
        if (emp == null) {
            return R.error("登录失败!");
        }

        //4.密码比对,密码不一致也返回登录失败结果

        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败!");
        }


        //5.查看员工状态
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用!");

        }

        //6.登录成功,将用户的id存入Session并返回登录结果
        //Sessison对象是Java Web应用程序的一个重要对象,作用是在服务器端存储用户的会话信息
        //用户第一次访问为其创建
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    //当接收到前端发送的HTTP请求logout就执行下面的退出方法
    @PostMapping("/logout")
    //参数的作用就是为了让该方法能够处理HTTP请求并响应
    public R<String> logout(HttpServletRequest request) {
        //清除Session(会话)保存的当前登录的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

}
