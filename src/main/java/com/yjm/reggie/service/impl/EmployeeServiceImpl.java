package com.yjm.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.entity.Employee;
import com.yjm.reggie.mapper.EmployeeMapper;
import com.yjm.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author 游锦民
 * @version 1.0
 */

    //服务层实现类,实现服务层接口,继承了ServiceImpl类,能够实现基本的CRUD操作
    //实现了EmployeeService接口需要实现该接口中定义的所有方法
    //泛型类中EmployeeMapper让实现类知道使用哪个Mapper接口实现对Employee进行数据访问和操作
    //泛型类中Employee是实体类,让ServiceImpl类知道要操作哪个实体类
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
