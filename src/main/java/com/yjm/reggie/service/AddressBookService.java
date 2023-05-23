package com.yjm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjm.reggie.entity.AddressBook;

/**
 * @author 游锦民
 * @version 1.0
 */
//继承了MybatisPlus的IService接口,IService接口继承了一些通用的数据库操作方法,通过AddressBookService接口可以对AddressBook实体类对应的数据库进行CRUD操作
public interface AddressBookService extends IService<AddressBook> {
}
