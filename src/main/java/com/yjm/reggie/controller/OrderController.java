package com.yjm.reggie.controller;

import com.yjm.reggie.common.R;
import com.yjm.reggie.entity.Orders;
import com.yjm.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 游锦民
 * @version 1.0
 */
@Slf4j
@Service
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submitOrder(@RequestBody Orders orders ){


        log.info("提交订单",orders);

        ordersService.submit(orders);
        return R.success("提交订单成功");
    }

}
