package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 游锦民
 * @version 1.0
 */
@Mapper //存储到Spirng容器中
public interface EmployeeMapper extends BaseMapper<Employee> {
//继承之后就能有增删改查基本功能了
}
