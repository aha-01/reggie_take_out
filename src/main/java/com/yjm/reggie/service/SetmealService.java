package com.yjm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjm.reggie.dto.SetmealDto;
import com.yjm.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author 游锦民
 * @version 1.0
 */
public interface SetmealService extends IService<Setmeal> {

    //添加接口方法
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐,同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
