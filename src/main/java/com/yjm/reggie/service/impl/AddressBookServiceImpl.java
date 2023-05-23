package com.yjm.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.reggie.entity.AddressBook;
import com.yjm.reggie.mapper.AddressBookMapper;
import com.yjm.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author 游锦民
 * @version 1.0
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
