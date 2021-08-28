package com.taiping.entity.riskmanage;

import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/10/30
 * 风险趋势分析统计实体
 */
@Data
public class RiskTrendStatistics {
    /**
     * 风险类型
     */
    private Integer riskType;
    /**
     * 风险等级
     */
    private Integer riskLevel;
    /**
     * 风险发生年份
     */
    private Integer riskFoundYear;
    /**
     * 风险发生月份
     */
    private Integer riskFoundMonth;
    /**
     * 数量
     */
    private Integer count;
}
