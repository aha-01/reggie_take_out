package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 游锦民
 * @version 1.0
 */
@Mapper //作用是生成一个实现类,实现了BaseMapper接口中的方法.是mybatis-plus的注解
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
