package com.yjm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yjm.reggie.common.BaseContext;
import com.yjm.reggie.common.R;
import com.yjm.reggie.entity.ShoppingCart;
import com.yjm.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author 游锦民
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 查看购物车
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车");
        //根据用户id查询购物车
        //获取当前用户的id
        Long currentId = BaseContext.getCurrentId();

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);

        //调用service方法查询购物车
        List<ShoppingCart> list = shoppingCartService.list(wrapper);

        //返回结果
        return R.success(list);
    }

    /**
     * 删除购物车
     *
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clear() {
        //获取当前用户的id
        Long currentId = BaseContext.getCurrentId();
        //根据用户id删除购物车
        LambdaUpdateWrapper<ShoppingCart> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, currentId);
        shoppingCartService.remove(wrapper);
        return R.success("清空成功");
    }


    //实现添加购物车
    //拦截路径上的add请求参数进行处理
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("添加购物车:{}", shoppingCart);
        /*
          思路：
          1.首先获取当前用户的id
          2.设置购物车的用户id
          3.根据商品id判断是菜品还是套餐
          4.如果是菜品，根据菜品id查询购物车中是否存在该菜品
          5.如果存在，更新购物车中的数量
          6.如果不存在，添加购物车
         *
         * */

        //获取当前用户的id
        Long currentId = BaseContext.getCurrentId();

        //设置购物车的用户id
        shoppingCart.setUserId(currentId);

        //获取当前菜品id
        Long dishId = shoppingCart.getDishId();
        //获取当前套餐id
        Long setMealId = shoppingCart.getSetmealId();


        //根据商品id判断是菜品还是套餐
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        if (dishId != null) {
            //如果是菜品，根据菜品id查询购物车中是否存在该菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //如果是套餐，根据套餐id查询购物车中是否存在该套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, setMealId);
        }

        //查询购物车中是否存在该商品
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);


        //如果购物车中存在该商品,更新购物车中的数量
        if (shoppingCartServiceOne != null) {
            //获取购物车中的数量
            Integer number = shoppingCartServiceOne.getNumber();

            //更新购物车中的数量
            shoppingCartServiceOne.setNumber(number + 1);

            //更新数据库,更新购物车中的数量
            shoppingCartService.updateById(shoppingCartServiceOne);

        } else {
            //添加时间
            shoppingCart.setCreateTime(LocalDateTime.now()); //为什么要使用LocalDateTime.now()?   因为数据库中的字段类型是datetime类型,而不是date类型

            //如果购物车中不存在该商品,添加购物车,数量默认为1
            shoppingCartService.save(shoppingCart);

            //统一返回结果
            shoppingCartServiceOne = shoppingCart;
        }

        //返回结果
        return R.success(shoppingCartServiceOne);
    }


}
