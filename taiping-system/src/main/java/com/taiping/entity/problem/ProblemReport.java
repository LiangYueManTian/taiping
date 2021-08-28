package com.taiping.entity.problem;

import lombok.Data;

import java.util.List;
/**
 * 问题分析分析报告实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-22
 */
@Data
public class ProblemReport {
    /**
     * 分析报告日期
     */
    private Long reportDate;
    /**
     * 故障总览次数统计
     */
    private List<ProblemStatistics> troubleLevel;
    /**
     * 故障总览趋势曲线
     */
    private ProblemTrend troubleTrend;
    /**
     * 次数统计
     */
    private List<ProblemStatistics> flashOff;
    /**
     * 停水停电趋势曲线
     */
    private ProblemTrend flashOffTrendDto;
    /**
     * 次数统计
     */
    private List<ProblemStatistics> troubleType;
    /**
     * 故障分类统计
     */
    private List<TroubleTypeStatistics> typeStatistics;
    /**
     * 故障分类趋势曲线
     */
    private TroubleTrendDto troubleTrendDto;
}
