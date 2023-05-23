package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 游锦民
 * @version 1.0
 */
@Mapper //指定这是一个操作数据库的mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
