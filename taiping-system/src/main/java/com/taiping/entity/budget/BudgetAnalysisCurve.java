package com.taiping.entity.budget;

import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/12/17
 * 预算分析曲线实体
 */
@Data
public class BudgetAnalysisCurve extends BudgetStatistics{
    /**
     *报告生成时间(精确到月)
     */
    private Long reportTime;
}
