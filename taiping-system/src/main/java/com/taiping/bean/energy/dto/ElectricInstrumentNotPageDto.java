package com.taiping.bean.energy.dto;

import lombok.Data;

/**
 * 总能耗不分页dto
 * @author hedongwei@wistronits.com
 * @date 2019/10/30 20:50
 */
@Data
public class ElectricInstrumentNotPageDto {

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 电量仪（度）
     */
    private String electricMeter;

    /**
     * 名称
     */
    private String name;

    /**
     * 设备类型
     */
    private String type;

    /**
     * 一月份用电数
     */
    private double januaryMeter;

    /**
     * 二月份用电数
     */
    private double februaryMeter;

    /**
     * 三月份用电数
     */
    private double marchMeter;

    /**
     * 四月份用电数
     */
    private double aprilMeter;

    /**
     * 五月份用电数
     */
    private double mayMeter;

    /**
     * 六月份用电数
     */
    private double juneMeter;

    /**
     * 七月份用电数
     */
    private double julyMeter;

    /**
     * 八月份用电数
     */
    private double augustMeter;

    /**
     * 九月份用电数
     */
    private double septemberMeter;

    /**
     * 十月份用电数
     */
    private double octoberMeter;

    /**
     * 十一月份用电数
     */
    private double novemberMeter;

    /**
     * 十二月份用电数
     */
    private double decemberMeter;

    /**
     * 年份
     */
    private Integer year;
}
