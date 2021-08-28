package com.taiping.bean.energy.analyze;

import lombok.Data;

/**
 * 能耗显示数据
 * @author hedongwei@wistronits.com
 * @date 2019/11/3 21:23
 */
@Data
public class EnergyViewData {

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private double value;
}
