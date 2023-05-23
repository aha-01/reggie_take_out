package com.yjm.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.dto.DishDto;
import com.yjm.reggie.entity.Dish;
import com.yjm.reggie.entity.DishFlavor;
import com.yjm.reggie.mapper.DishMapper;
import com.yjm.reggie.service.DishFlavorService;
import com.yjm.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品,同时保存对应的口味数据
     *
     * @param dishDto
     */
    @Transactional //方法中的数据库操作将在一个事务中执行，要么全部成功提交，要么全部回滚。
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flaver
        dishFlavorService.saveBatch(flavors);

    }

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息,从dish表查询
        Dish dish = this.getById(id);

        //复制菜品信息到继承类中
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询当前菜品对应的口味信息,从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        //表示查询属性getDishId值等于dish中的id的记录
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());

        //dishFlavorService调用其中的list方法并传入查询条件queryWrapper,执行对dish_flavor表的查询操作
        //list方法把查询结果封装成一个List<DishFlavor>对象,将查询结果保存到flavor中
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    //保持事务一致性
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表的基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        //删除
        dishFlavorService.remove(queryWrapper);


        //添加当前提交过来的口味数据,dish_flavor表的insert操作
        //获取口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        //遍历每一项口味数据设置dish的Id
        //dishDto对象综合保存了新增的菜品信息其中包含了自动生成的ID值,因此可以将这个ID值作为所有新增口味的dishId值
        //map方法遍历列表分钟的每一项口味数据
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        //saveBatch 方法可以将一个包含多个元素的列表一次性插入到数据库中
        dishFlavorService.saveBatch(flavors);
    }
}
