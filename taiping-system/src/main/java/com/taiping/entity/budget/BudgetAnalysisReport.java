package com.taiping.entity.budget;

import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/12/10
 * 预算分析报告实体
 */
@Data
public class BudgetAnalysisReport {
    /**
     *主键id
     */
    private String tid;
    /**
     *分析对象
     */
    private String analysisObject;
    /**
     *预算金额
     */
    private Double budgetAmount;
    /**
     *成交金额
     */
    private Double dealAmount;
    /**
     *执行比例
     */
    private Double ratio;
    /**
     *剩余金额
     */
    private Double surplusAmount;
    /**
     *相关运维管理活动id
     */
    private String manageId;
    /**
     * 处理说明
     */
    private String solveInstruction;
    /**
     *运维管理活动类型
     */
    private String activityType;
    /**
     * 是否为年度总预算
     */
    private Integer isYearBudget;
    /**
     *报告生成时间(精确到月)
     */
    private Long reportTime;
}
