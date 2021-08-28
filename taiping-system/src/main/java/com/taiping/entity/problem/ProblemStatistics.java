package com.taiping.entity.problem;

import lombok.Data;

/**
 * 问题分析次数统计实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-18
 */
@Data
public class ProblemStatistics {
    /**
     * 主键
     */
    private String statisticsId;
    /**
     * 统计类别
     */
    private String statisticsType;
    /**
     * 数据类别
     */
    private String valueType;
    /**
     * 数据类别次数
     */
    private Integer valueNumber;
    /**
     * 环比
     */
    private String monthPercentage;
    /**
     * 同比
     */
    private String yearPercentage;
    /**
     * 数据日期
     */
    private Long valueDate;
    /**
     * 同比环比描述
     */
    private String instruction;
    /**
     * 关联运维管理活动ID
     */
    private String manageId;
    /**
     * 产生对象
     */
    private String sourceName;
    /**
     * 产生对象Code
     */
    private String sourceCode;
    /**
     * 产生原因
     * */
    private String cause;
    /**
     * 处理说明
     */
    private String solveInstruction;
    /**
     *运维管理活动类型
     */
    private String activityType;
    /**
     * 报告类型
     */
    private String reportType;
}
