package com.yjm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjm.reggie.entity.Orders;

/**
 * @author 游锦民
 * @version 1.0
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders
     */
    public void submit(Orders orders);
}
