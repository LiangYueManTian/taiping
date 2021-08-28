package com.taiping.entity.analyze;

import lombok.Data;

/**
 * 分析运维管理数据
 * @author hedongwei@wistronits.com
 * @date 2019/11/19 14:17
 */
@Data
public class AnalyzeThresholdBaseInfo {

    /**
     * 阈值id
     */
    private String thresholdInfoId;

    /**
     * 阈值名称
     */
    private String thresholdName;

    /**
     * 阈值的值
     */
    private String thresholdValue;

    /**
     * 阈值编码
     */
    private String thresholdCode;

    /**
     * 具体阈值的对象
     */
    private String thresholdData;


    /**
     * 类型  （容量分析  阈值类型  1 楼层  2 机柜 3 功能区 4 模块 5 ups 6 机柜列 7 机柜（PDU） 8 路由类型）
     *       （能耗分析  分析类型  1 冷机  2 水泵 3 冷塔 4 精密空调 5 it能耗 6 动力能耗 7 pue值）
     */
    private String type;

    /**
     * 模块  （容量分析 模块  1 空间容量 2 电力容量 3 综合布线）
     *        (能耗分析 模块  1 暖通分项 2 数据机房 3 pue值)
     */
    private String module;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 年份
     */
    private Integer year;
}
