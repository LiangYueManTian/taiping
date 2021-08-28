package com.taiping.entity.analyze.energy;

import lombok.Data;

import java.util.List;

/**
 * 能耗分析报告阈值信息
 *
 * @author hedongwei@wistronits.com
 * @date 2019/11/15 16:33
 */
@Data
public class EnergyAnalyzeInfo {

    /**
     * 阈值信息id
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
     * 同比比率
     */
    private double yearOverYearPercent;

    /**
     * 具体阈值的对象
     */
    private String thresholdData;


    /**
     * 环比比率
     */
    private double ringGrowth;

    /**
     * 阈值类型  1 冷机  2 水泵 3 冷塔 4 精密空调 5 it能耗 6 动力能耗   7 pue值
     */
    private String type;

    /**
     * 模块  1 暖通分项 2 数据机房 3 pue值
     */
    private String module;

    /**
     * 建议
     */
    private String advice;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 是否是子项
     */
    private String isChild;


    /**
     * 父数据code
     */
    private String parentCode;

    /**
     * 同比数量
     */
    private double yearOverYearNumber;

    /**
     * 环比数量
     */
    private double ringGrowthNumber;


    /**
     * 运维管理活动类型
     */
    private String activityType;


    /**
     * 管理类型
     */
    private String manageType;

    /**
     * 处理说明
     */
    private String solveInstruction;

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
     * 数据采集时间
     */
    private Long dataCollectionTime;

    /**
     * 0 未删除 1 已删除
     */
    private Integer isDeleted;

    /**
     * 创建用户
     */
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private String updateTime;
    /**
     * 子集树结构
     */
    private List<EnergyAnalyzeInfo> childList;

}
