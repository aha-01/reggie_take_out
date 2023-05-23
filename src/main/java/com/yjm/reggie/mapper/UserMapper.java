package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 游锦民
 * @version 1.0
 */
//继承BaseMapper接口说明这是一个mp提供的通用Mapper接口,泛型参数说明操作的是User实体类,User实体类中的属性对应数据库中的字段,mp会自动完成增删改查操作
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
