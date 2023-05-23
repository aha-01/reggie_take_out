package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.reggie.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 游锦民
 * @version 1.0
 */
//目的是为了对数据库进行增删改查

//@Mapper注解会告诉框架根据接口定义生成对应的实现类
//继承BaseMapper<SetmealDish>接口,SetmealDishMapper继承一些通用的数据库操作方法,通过SetmealDishMapper接口可以对SetmealDish 实体类对应的数据库进行CRUD操作
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
}
