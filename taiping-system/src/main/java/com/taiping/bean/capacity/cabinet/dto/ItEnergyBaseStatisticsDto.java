package com.taiping.bean.capacity.cabinet.dto;

import lombok.Data;

/**
 * it能耗统计父类
 * @author hedongwei@wistronits.com
 * @date 2019/11/7 15:43
 */
@Data
public class ItEnergyBaseStatisticsDto {

    /**
     * 模块编码
     */
    private String module;

    /**
     * 类型
     */
    private String type;


    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 增长值
     */
    private double growthElectricMeter;

    /**
     * 数据占比
     */
    private double dataPercent;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;
}
