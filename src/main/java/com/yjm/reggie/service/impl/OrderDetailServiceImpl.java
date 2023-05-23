package com.yjm.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.entity.OrderDetail;
import com.yjm.reggie.mapper.OrderDetailMapper;
import com.yjm.reggie.mapper.OrderMapper;
import com.yjm.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 游锦民
 * @version 1.0
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService { //
}
