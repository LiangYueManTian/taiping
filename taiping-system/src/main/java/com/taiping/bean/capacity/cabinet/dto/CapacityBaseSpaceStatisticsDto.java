package com.taiping.bean.capacity.cabinet.dto;

import lombok.Data;

/**
 * @author hedongwei@wistronits.com
 * @date 2019/11/6 9:17
 */
@Data
public class CapacityBaseSpaceStatisticsDto {

    /**
     * 设计空间
     */
    private Integer designSpaceCapacity;

    /**
     * 已用空间
     */
    private Integer usedSpaceCapacity;

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
     * 已用空间容量百分比
     */
    private double usedSpacePercent;

    /**
     * 已用实际电力容量百分比
     */
    private double usedActualPercent;
}
