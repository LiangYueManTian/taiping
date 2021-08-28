package com.taiping.bean.energy.parameter;

import com.taiping.entity.energy.PowerEnergyItem;
import lombok.Data;

import java.util.List;

/**
 * 动力能耗参数
 * @author hedongwei@wistronits.com
 * @date 2019/10/28 17:02
 */
@Data
public class PowerEnergyListParameter extends PowerEnergyItem {

    /**
     * 设备类型集合
     */
    private List<String> typeList;

    /**
     * 年份集合
     */
    private List<Integer> yearList;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;
}
