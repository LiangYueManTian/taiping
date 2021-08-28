package com.taiping.entity.analyze;

import lombok.Data;

/**
 * 分析关联显示数据父类
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 14:12
 */
@Data
public class AnalyzeBaseRelatedViewInfo {

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
     * 模块类型 1 空间容量 2 电力容量 3 综合布线
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
     * 0 未删除 1 已删除
     */
    private Integer isDeleted;
}
