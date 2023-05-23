package com.yjm.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.common.CustomException;
import com.yjm.reggie.dto.SetmealDto;
import com.yjm.reggie.entity.Setmeal;
import com.yjm.reggie.entity.SetmealDish;
import com.yjm.reggie.mapper.SetmealMapper;
import com.yjm.reggie.service.SetmealDishService;
import com.yjm.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 游锦民
 * @version 1.0
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {


    //注入操作关联关系
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐,同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息,操作setmeal,执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());


        //保存套餐和菜品的关联信息,操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 删除套餐,同时需要删除套餐和菜品的关联数据
     *
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in(1,2,3) and status = 1

        //删除逻辑
        //查询套餐状态,确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        //判断Setmeal实体类中getId字段的值时候在ids列表中
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        //判断Setmeal实体类的getId字段的值是否等于1
        long count = this.count(queryWrapper);

        if (count > 0) {

            //如果不能删除就抛出一个业务异常
            throw new CustomException("套餐正在售卖中,不能删除");
        }


        //如果可以删除就先删除套餐中的数据--setmeal
        //this.removeBatchByIds(ids)的作用是删除调用该方法的对象对应的套餐数据。
        this.removeBatchByIds(ids);


        //再删除关系表中的数据---setmeal_dish
        //delete frp,setmeal_dish where setmeal_id in(1,2,3)
        //创建条件构造器
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);

        //删除操作
        setmealDishService.remove(lambdaQueryWrapper);
    }
}
