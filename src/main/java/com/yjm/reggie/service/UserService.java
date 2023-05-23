package com.yjm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjm.reggie.common.R;
import com.yjm.reggie.entity.User;

/**
 * @author 游锦民
 * @version 1.0
 */
//MP提供的通用Service接口,继承了IService接口,泛型参数说明操作的是User实体类,User实体类中的属性对应数据库中的字段,mp会自动完成增删改查操作

public interface UserService extends IService<User> {

}
