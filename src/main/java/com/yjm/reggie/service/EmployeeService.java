package com.yjm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjm.reggie.entity.Employee;

/**
 * @author 游锦民
 * @version 1.0
 */
public interface EmployeeService extends IService<Employee> {
    //继承IService接口，这是一个MyBatis-Plus提供的通用Service接口，包含了一些基本的增删改查方法，可以直接使用而不需要写SQL语句123。
}