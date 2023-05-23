package com.yjm.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.common.BaseContext;
import com.yjm.reggie.entity.*;
import com.yjm.reggie.mapper.OrderMapper;
import com.yjm.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class OrdersServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrdersService {

    //注入用户服务
    @Autowired
   private UserService userService;

    //注入购物车服务
    @Autowired
    private ShoppingCartService shoppingCartService;

    //注入地址服务
    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public void submit(Orders orders) {

        /*
        * 思路:
        * 1.首先获取到用户id
        * 2.根据用户id查询购物车
        * 3.判断购物车是否为空
        * 4.判断地址是否错误,主要是提交的地址和用户id的地址是否匹配
        * 5.获取用户信息,为后面赋值
        * 6.向订单细节表设置属性
        * 7.向订单表设置属性
        *
        * */

        //获取当前用户id
        Long userId = BaseContext.getCurrentId();

        //根据用户id查询购物车,拿到了用户的id就可以根据id进行查询,这里使用的是lambda表达式,
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //参数1:ShoppingCart::getUserId,参数2:userId,参数1的作用是获取购物车的用户id,参数2是获取到的当前用户的id
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        //查询购物车,list是查询多条数据,如果是查询一条数据,使用getOne
        List<ShoppingCart> ShoppingCartlist = shoppingCartService.list(queryWrapper);

        //判断购物车是否为空
        if (ShoppingCartlist.isEmpty()){
            throw new RuntimeException("购物车为空");
        }

        //判断地址是否错误,主要是提交的地址和用户id的地址是否匹配
        Long addressBookId = orders.getAddressBookId();

        //根据地址id查询地址
        AddressBook addressBook = addressBookService.getById(addressBookId);
        //判断地址是否为空
        if (addressBook == null){
            throw new RuntimeException("地址错误");
        }

        //获取用户信息,为后面赋值,根据用户id获取到用户信息
        User user = userService.getById(userId);

        //自动生成订单号
        long orderId = IdWorker.getId();//生成唯一id,保证原始性,是谁提供的?雪花算法,MYBATIS-PLUS提供的

        //保证原始性
        AtomicInteger amount = new AtomicInteger(0); //原子类,保证原始性,是谁提供的?JUC提供的

        //向订单细节表设置属性
        //item 是流中的每个元素，而 (item) -> { ... } 是传递给 map() 方法的函数，用于对每个元素进行映射操作。
        List<OrderDetail> orderDetailList= ShoppingCartlist.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setAmount(item.getAmount());
            //计算总金额,原子类,保证原始性,是谁提供的?JUC提供的
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());

            //返回订单细节
            return orderDetail;
        }).collect(Collectors.toList());//将流转换成集合



        //向订单表设置属性
        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setAddressBookId(addressBookId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setPhone(addressBook.getPhone());
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(
                (addressBook.getProvinceName() == null ? "":addressBook.getProvinceName())+
                        (addressBook.getCityName() == null ? "":addressBook.getCityName())+
                        (addressBook.getDistrictName() == null ? "":addressBook.getDistrictName())+
                        (addressBook.getDetail() == null ? "":addressBook.getDetail())
        );

        //最后保存订单
        //保存订单
        this.save(orders);

        //保存订单细节,savebatch是批量保存,参数是一个集合
        orderDetailService.saveBatch(orderDetailList);

        //保存之后,删除购物车
        shoppingCartService.remove(queryWrapper);

    }
}
