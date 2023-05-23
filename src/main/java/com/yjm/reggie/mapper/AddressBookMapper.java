package com.yjm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 游锦民
 * @version 1.0
 */

//继承了MybatisPlus的BaseMapper接口
@Mapper //说明这是一个Mapper接口,作用类似于spring的@Repository注解
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
