package com.yjm.reggie.dto;

import com.yjm.reggie.entity.Setmeal;
import com.yjm.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
