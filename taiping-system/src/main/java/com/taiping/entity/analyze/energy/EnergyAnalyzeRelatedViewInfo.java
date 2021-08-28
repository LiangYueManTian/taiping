package com.taiping.entity.analyze.energy;

import lombok.Data;

/**
 * 能耗分析报告关联显示图表
 * @author hedongwei@wistronits.com
 * @date 2019/11/1 9:24
 */
@Data
public class EnergyAnalyzeRelatedViewInfo {

    /**
     * 阈值关联id
     */
    private String thresholdRelatedViewId;

    /**
     * 阈值code
     */
    private String thresholdCode;

    /**
     * 类型 1 显示图数据 2 趋势数据
     */
    private String viewType;

    /**
     * 数据code
     */
    private String dataCode;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 值
     */
    private double value;

    /**
     * 数据模块
     */
    private String dataModule;

    /**
     * 模块类型
     */
    private String moduleType;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 数据时间
     */
    private Long dataTime;

    /**
     * 分析年份
     */
    private Integer analyzeYear;

    /**
     * 分析月份
     */
    private Integer analyzeMonth;

    /**
     * 分析时间
     */
    private Long analyzeTime;

    /**
     * 0 未删除 1 已删除
     */
    private Integer isDeleted;
}
