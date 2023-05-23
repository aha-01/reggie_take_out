package com.yjm.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.entity.ShoppingCart;
import com.yjm.reggie.mapper.ShoppingCartMapper;
import com.yjm.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;



/**
 * @author 游锦民
 * @version 1.0
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
