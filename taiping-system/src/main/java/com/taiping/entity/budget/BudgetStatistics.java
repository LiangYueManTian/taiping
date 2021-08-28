package com.taiping.entity.budget;

import lombok.Data;

/**
 * @author zhangliangyu
 * @since 2019/12/17
 * 预算统计实体
 */
@Data
public class BudgetStatistics {
    /**
     * 主键id
     */
    private String id;
    /**
     * 统计对象
     */
    private String statisticsObject;
    /**
     *执行比例
     */
    private Double ratio;
    /**
     * 是否为年度总预算
     */
    private Integer isYearBudget;
    /**
     * 统计时间
     */
    private Long statisticsTime;
}
