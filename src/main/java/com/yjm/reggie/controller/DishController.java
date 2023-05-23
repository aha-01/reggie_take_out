package com.yjm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjm.reggie.common.R;
import com.yjm.reggie.dto.DishDto;
import com.yjm.reggie.entity.Category;
import com.yjm.reggie.entity.Dish;
import com.yjm.reggie.entity.DishFlavor;
import com.yjm.reggie.service.CategoryService;
import com.yjm.reggie.service.DishFlavorService;
import com.yjm.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish") //打开前端页面的控制台发现请求网址: http://localhost:8080/dish/page?page=1&pageSize=10,所以拦截路径是/dish
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping//不要URL,因为请求就是/dish
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功!");
    }

    /**
     * 菜品信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        //创建分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();


        //条件构造器
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        // 添加过滤条件，即根据传入的 name 进行模糊匹配查询，只有在 name 不为 null 时才添加该过滤条件
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name);

        //添加排序条件,根据菜品对象的更新时间进行降序排序
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        //执行分页查询
        //参数1:分页构造器包含分页查询所需的的页码和每页记录数
        //参数2:条件构造器包含指定查询的过滤条件和排序规则
        dishService.page(pageInfo, dishLambdaQueryWrapper);

        //对象拷贝,给dishDtoPage赋值
        //List<T> records集合不用拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        // // 获取分页查询结果中的记录列表 records
        List<Dish> records = pageInfo.getRecords();

        //处理记录列表records, 将每个菜品对象转换为相应的 DishDto 对象,并关联查询菜品分类名称
        List<DishDto> list = records.stream().map((item) -> { //item代表的是Dish,遍历出来的每一个菜品对象

            DishDto dishDto = new DishDto();

            // 将菜品对象的属性拷贝到 DishDto 对象中
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//获取菜品对象的分类Id

            //目的拿着分类id去查分类表把名称查出来
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();//获取分类对象的名称

                //把菜品分类名称赋值给dishDto对象的菜品分类名称属性
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;

        }).collect(Collectors.toList());


        //将处理后的记录列表设置dishDtoPage 对象的记录集合
        dishDtoPage.setRecords(list);

        //返回成功的响应对象,包含分页查询结果
        return R.success(dishDtoPage);

    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}") //添加{}因为是路径变量为了接收不同的id,路径变量是在URL路径中的一部分，用于动态地接收参数值。
    //使用DishDto参数是因为其中继承了Dish并且包含了口味DishFlavor这个属性
    public R<DishDto> get(@PathVariable Long id){ //@PathVariable Long id 表示将 URL 路径中的 id 变量提取出来，并将其作为 Long 类型的参数传递给该方法。

        //调用查询方法
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);

    }

    /**
     * 修改菜品信息
     *
     * @param dishDto
     * @return
     */
    @PutMapping//不要URL,因为请求就是/dish
    public R<String> update(@RequestBody DishDto dishDto) {


        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功!");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
/*    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //构造查询条件.使用lambada表达式
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());

        //条件查询,查询状态为1(起售状态)
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        //根据getSort升序排,根据getUpdateTime降序排
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);

    }*/

    /**
     *  根据条件查询对应的菜品数据,并显示菜品口味
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件.使用lambada表达式
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());

        //条件查询,查询状态为1(起售状态)
        queryWrapper.eq(Dish::getStatus,1);


        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishes = dishService.list(queryWrapper);

        //使用集合stream流的方式遍历dishes集合,将每个菜品对象转换为DishDto对象
        List<DishDto> list = dishes.stream().map((item) -> {
            //创建DishDto对象
            DishDto dishDto = new DishDto();
            //将菜品对象的属性拷贝到DishDto对象中
            BeanUtils.copyProperties(item,dishDto);

            //根据菜品id查询对应的口味列表
            List<DishFlavor> dishFlavorList = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, item.getId()));

            //将口味列表设置到dishDto对象中
            dishDto.setFlavors(dishFlavorList);

            //返回dishDto对象
            return dishDto;

            //将处理后的dishDto对象收集到集合中
        }).collect(Collectors.toList());


        return R.success(list);

    }
}
