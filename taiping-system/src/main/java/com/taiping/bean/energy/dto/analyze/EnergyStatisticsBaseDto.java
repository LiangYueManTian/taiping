package com.taiping.bean.energy.dto.analyze;

import lombok.Data;

/**
 * 能耗统计父类
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 11:18
 */
@Data
public class EnergyStatisticsBaseDto {

    /**
     * 月份
     */
    private Integer month;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 全部增长电量
     */
    private double growthElectricMeter;

    /**
     * 数据code
     */
    private String dataCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 数据名称
     */
    private String dataName;


    /**
     * 类型
     */
    private String type;

}
