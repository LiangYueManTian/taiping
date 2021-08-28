package com.taiping.entity.budget;

import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/12/11
 * 采购分析报告实体
 */
@Data
public class PurchaseAnalysisReport {
    /**
     *主键id
     */
    private String tid;
    /**
     * 采购项目id
     */
    private String purchaseId;
    /**
     * 采购项目名称
     */
    private String purchaseProName;
    /**
     * 项目预算金额
     */
    private Double budgetAmount;
    /**
     * 成交金额金额
     */
    private Double dealAmount;
    /**
     * 产生运维管理活动原因
     */
    private String manageCause;
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
     *报告生成时间(精确到月)
     */
    private Long reportTime;
}
