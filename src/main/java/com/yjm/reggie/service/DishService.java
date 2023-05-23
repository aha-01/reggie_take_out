package com.yjm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjm.reggie.dto.DishDto;
import com.yjm.reggie.entity.Dish;

/**
 * @author 游锦民
 * @version 1.0
 */

public interface DishService extends IService<Dish> {

    //新增菜品,同时插入菜品对应的口味数据,需要操作两张表,dish,dish_flaver
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //修改菜品信息,需要操作两张表
    public void updateWithFlavor(DishDto dishDto);
}
