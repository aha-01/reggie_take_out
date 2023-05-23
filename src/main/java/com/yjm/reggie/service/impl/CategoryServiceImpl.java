package com.yjm.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.common.CustomException;
import com.yjm.reggie.entity.Category;
import com.yjm.reggie.entity.Dish;
import com.yjm.reggie.entity.Setmeal;
import com.yjm.reggie.mapper.CategoryMapper;
import com.yjm.reggie.service.CategoryService;
import com.yjm.reggie.service.DishService;
import com.yjm.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 游锦民
 * @version 1.0
 */
//继承ServiceImpl类，这是一个MyBatis-Plus提供的通用Service实现类，包含了一些基本的增删改查方法的实现，可以直接使用而不需要写SQL语句
//使用泛型参数<M,T>来指定ServiceImpl类操作的Mapper接口和实体类类型


@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    /**
     * 根据id删除分类，删除前判断
     *
     * @param id
     */
    //注入菜单和套餐属性
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long ids) {
        /*
         * 思路
         * 1.注入菜品和套餐属性
         * 2.可以通过数据库查询菜单中每一款菜和菜品分类中关联id有多少种
         * 3.例如川菜类别就和6个菜有关联,所以当删除川菜的前提需要把这6个菜都删空,不然就啥也别想动
         * 4.1 所以直接在菜单中判断是否有该分类的类别,如果大于0就抛出异常不进行删除
         * 4.2 异常要自定义在common包下继承运行时异常
         * 5.在全局异常处理类中创建方法响应给页面
         * */

        //遍历菜单查询id是否管理分类
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件,根据分类id进行查询
        //参数2是要删除的分类id
        //参数1是lambda表达式，它把一个Dish对象映射到它的categoryId属性
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);

        long count1 = dishService.count(dishLambdaQueryWrapper);

        if (count1 > 0) {
            //查询当前菜品是否关联了菜品,如果已经关联,抛出一个异常
            throw new CustomException("当前分类下关联了菜品,不能删除");

        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, ids);

        long count2 = setmealService.count(setmealLambdaQueryWrapper); //通过setmealService接口可以提高代码的可读性和可维护性

        if (count2 > 0) {
            //查询当前菜品是否关联了套餐,如果已经关联,抛出一个异常
            throw new CustomException("当前分类下关联了套餐,不能删除");

        }

        //正常删除分类
        super.removeById(ids);

    }

}
