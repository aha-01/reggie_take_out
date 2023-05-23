package com.yjm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.entity.ShoppingCart;
import com.yjm.reggie.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
 * @author 游锦民
 * @version 1.0
 */

//MP提供的通用Service接口,继承了IService接口,泛型参数说明操作的是ShoppingCart实体类,ShoppingCart实体类中的属性对应数据库中的字段,mp会自动完成增删改查操作
public interface ShoppingCartService extends IService<ShoppingCart> {
}