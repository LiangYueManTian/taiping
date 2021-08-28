package com.taiping.entity.riskmanage;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/11/13
 * 风险现状分析报告实体
 */
@Data
@TableName("t_risk_current_analysis_report")
public class RiskCurrentAnalysisReport extends RiskCurrentAnalysis{
    /**
     * 报告生成时间
     */
    private Long generationTime;
}
