package com.taiping.bean.energy;

import lombok.Data;

/**
 * 能耗父表实体类
 * @author hedongwei@wistronits.com
 * @date 2019/11/14 15:24
 */
@Data
public class EnergyBaseBean {

    /**
     * 数据的code
     */
    private String dataCode;

    /**
     * 累计电量数
     */
    private double allElectricMeter;

    /**
     * 增长电量数
     */
    private double growthElectricMeter;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;
}
