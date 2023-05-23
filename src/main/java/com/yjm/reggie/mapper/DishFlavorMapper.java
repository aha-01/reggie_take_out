package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 游锦民
 * @version 1.0
 */

@Mapper
//@Mapper注解标识为数据访问层,Dao层,会让Mybatis在运行时生成该接口的实现类
//继承 BaseMapper<DishFlavor> 表示示 DishFlavorMapper 接口从 BaseMapper 接口继承了通用的数据库操作方法。
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
