package com.yjm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjm.reggie.common.R;
import com.yjm.reggie.dto.SetmealDto;
import com.yjm.reggie.entity.Category;
import com.yjm.reggie.entity.Setmeal;
import com.yjm.reggie.service.CategoryService;
import com.yjm.reggie.service.SetmealDishService;
import com.yjm.reggie.service.SetmealService;
import com.yjm.reggie.service.impl.SetmealServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


//因为操作的主表还是这个Setmeal套餐表,如果要操作关联关系统一在这调用即可
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    //注入套餐表和套餐关系表
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;






    /**
     * 根据条件查询套餐列表
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<Setmeal>()
                //根据菜品id进行查询,不然商务和儿童套餐都会被查询出来
                .eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId())
                .eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus())
                .orderByDesc(Setmeal::getUpdateTime);

        //查询所有的套餐
        List<Setmeal> list = setmealService.list(wrapper);

        return R.success(list);
    }


    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)//当新增套餐的时候,清空缓存
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息: {}", setmealDto);

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }


    /**
     * 套餐分页查询
     *
     * @param page 当前页数
     * @param pageSize 每页的数据量
     * @param name 套餐名称的模糊查询
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //分页构造器对象,用于分页查询结果的封装
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page, pageSize);

        //查询条件的包装器对象
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件,根据name进行like模糊查询
        queryWrapper.like(name != null, Setmeal::getName, name);

        //添加排序条件,根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //进行分页查询,将查询结果封装到pageInfo中
        setmealService.page(pageInfo, queryWrapper);

        //拷贝对象,除了records属性
        //原因是dtoPage对象是作为返回结果的,而records属性是后续的逻辑中进行处理和设置的,因此不希望直接被拷贝
        //排除records属性的目的是确保dtoPage对象在拷贝属性值时保持原样，不受pageInfo对象的影响，以便在后续的代码中对records属性进行处理和设置。
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        //从pageInfo中获取查询到的套餐列表records
        List<Setmeal> records = pageInfo.getRecords();

        //使用stream().map()对套餐列表records进行处理,将每个套餐对象转换成对应的SetmealDto对象
        List<SetmealDto> list = records.stream().map((item) -> {

            SetmealDto setmealDto = new SetmealDto();

            //对象拷贝
            BeanUtils.copyProperties(item, setmealDto);

            //分类id
            Long categoryId = item.getCategoryId();

            //根据分类id查询对象分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());


        dtoPage.setRecords(list);
        //使用R.success(dtoPage)将封装好的结果返回，R.success()是一个用于构建成功响应的辅助方法。
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){ //@RequestParam表示将请求中名为ids参数的值绑定到方法的ids参数上
        //使用@RequestParam注解，可以获取DELETE请求中的参数值，并将其传递给方法进行处理
        log.info("ids:{}",ids);

        //调用删除操作
        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功");
    }
}
