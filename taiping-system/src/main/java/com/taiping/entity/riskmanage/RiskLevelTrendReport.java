package com.taiping.entity.riskmanage;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/11/13
 * 风险等级趋势分析报告实体
 */
@Data
@TableName("t_risk_level_trend_report")
public class RiskLevelTrendReport extends RiskLevelTrend{
    /**
     * 报告生成时间
     */
    private Long generationTime;
}
