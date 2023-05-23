package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.omg.CORBA.BAD_CONTEXT;

/**
 * @author 游锦民
 * @version 1.0
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> { //继承BaseMapper接口说明这是一个mp提供的通用Mapper接口
}
