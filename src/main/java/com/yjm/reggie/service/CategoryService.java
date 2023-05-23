package com.yjm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjm.reggie.entity.Category;

/**
 * @author 游锦民
 * @version 1.0
 */
//这是什么接口?这是一个MP提供的通用Service接口,继承了IService接口
public interface CategoryService extends IService<Category> {
    public void remove(Long ids);
}
