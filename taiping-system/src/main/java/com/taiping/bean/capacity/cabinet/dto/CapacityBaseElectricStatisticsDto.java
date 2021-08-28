package com.taiping.bean.capacity.cabinet.dto;

import lombok.Data;

/**
 * 电力容量查询父类
 * @author hedongwei@wistronits.com
 * @date 2019/11/6 9:17
 */
@Data
public class CapacityBaseElectricStatisticsDto {

    /**
     * 月份
     */
    private Integer month;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 设计功率
     */
    private double ratedPower;

    /**
     * 已用实际功率
     */
    private double usedActualPower;

    /**
     * 已用实际电力容量百分比
     */
    private double usedActualPercent;
}
