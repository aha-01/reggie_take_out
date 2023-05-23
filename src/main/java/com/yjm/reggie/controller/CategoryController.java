package com.yjm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjm.reggie.common.R;
import com.yjm.reggie.entity.Category;
import com.yjm.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 游锦民
 * @version 1.0
 */


@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        //MyBatis-Plus提供的通用Service方法.保存category对象到数据库
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //添加排序条件,根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //分页查询
        categoryService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    //
    //实现简单删除
    //
    //

    /**
     * 删除菜品分类
     *
     * @param ids 参数为ids,可以查看接收到的URL请求后面的路径
     * @return 返回的类型是String 原因是前端代码的回调函数返回一个code就行，而R对象就有code
     */
    @DeleteMapping
    public R<String> delete(Long ids) {

        log.info("删除分类,id 为: {}", ids);

        //自定义的删除方法
        categoryService.remove(ids);

        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category 参数为整个菜品,因为PUT请求发送的是JSON数据
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息: {}", category);

        //修改菜品
        categoryService.updateById(category);

        //注意:updatetime与updateUser已经在实体类中添加@TableField使其可以通过自定义元数据处理器自动填充

        return R.success("信息修改成功");

    }


    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        //添加条件
        //调用queryWrapper的eq方法，判断category对象的type属性是否不为空，如果不为空，则添加一个等值条件，
        // 即Category表的type字段等于category对象的type属性值。

        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());

        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        //通过服务层调用MP提供的数据库操作
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);

    }


}
