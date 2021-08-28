package com.taiping.entity.productivity;

import lombok.Data;

/**
 * 团队负荷月份统计数据实体类
 *
 * @author chaofang@wistronits.com
 * @since 2019-11-11
 */
@Data
public class MonthWorkload {
    /**
     * 主键id
     */
    private String workloadId;
    /**
     * 类型（预测或实际）
     */
    private String valueType;
    /**
     * 日期
     */
    private Long workloadDate;
    /**
     * 负荷类型
     */
    private String workloadType;
    /**
     * 负荷比
     */
    private String workloadPercentage;
    /**
     * 环比
     */
    private String monthPercentage;
    /**
     * 同比
     */
    private String yearPercentage;
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
