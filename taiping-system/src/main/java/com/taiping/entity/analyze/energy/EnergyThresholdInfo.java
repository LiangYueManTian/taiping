package com.taiping.entity.analyze.energy;

import com.taiping.entity.analyze.AnalyzeThresholdBaseInfo;
import lombok.Data;

/**
 * 能耗阈值信息
 * @author hedongwei@wistronits.com
 * @date 2019/11/15 16:33
 */
@Data
public class EnergyThresholdInfo extends AnalyzeThresholdBaseInfo {

    /**
     * 同比比率
     */
    private double yearOverYearPercent;

    /**
     * 环比比率
     */
    private double ringGrowth;

    /**
     * 建议
     */
    private String advice;

    /**
     * 是否是子项
     */
    private String isChild;

    /**
     * 父级code
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
     * 数据采集时间
     */
    private Long dataCollectionTime;

    /**
     * 原因
     */
    private String cause;

    /**
     * 是否统计
     */
    private boolean isStatistical;

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
    private Long createTime;

    /**
     * 修改用户
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private Long updateTime;


}
