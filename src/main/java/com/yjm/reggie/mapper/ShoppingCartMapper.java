package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.yjm.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 游锦民
 * @version 1.0
 */

@Mapper //作用是将接口声明为一个Mapper接口,使它可以被SpringBoot扫描到
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
